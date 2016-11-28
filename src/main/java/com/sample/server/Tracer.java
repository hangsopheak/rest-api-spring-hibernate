package com.sample.server;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * A tracer that allows performance tracing at millisecond and microsecond precision. This class is safe
 * to use in a multi-threaded environment.  The data used for
 * recording information is held via a ThreadLocal variable.  To use this class then, it's usually best to use
 * the pattern below at the entry & exit points into your application.
 * <code>
 * Tracer.create();
 * try {
 *   // anywhere throughout the rest of the application at this point may make any Tracer API calls
 *   // desired.
 * } finally {
 * 	 Tracer.destroy();
 * }
 * </code>
 * <p/>
 * Example of use for milliseconds:
 * Tracer.mark("start:doSomething");
 * Tracer.mark("start:innerMethod");
 * Tracer.mark("end:innerMethod");
 * Tracer.markAndOutput("end:doSomething");
 * <p/>
 * Example of use for microseconds:
 * Tracer.markInMicros("step1");
 * Tracer.markInMicros("step2");
 * Tracer.markAndOutputInMicros("step3");
 * <p/>
 * Output from the last line in both of the above examples will look something like the following:
 * ----- TRACE -----
 *   dealMade:start 0 micros. [0 micros.]
 *   dealMade:start-sync 64 micros. [64 micros.]
 *   dealMade:end-sync 445 micros. [510 micros.]
 *   dealMade:start-ack 18 micros. [528 micros.]
 *   dealMade:end-ack 5004 micros. [5533 micros.]
 *   dealMade:end 402 micros. [5935 micros.]
 *
 */
public final class Tracer {
	
	static final long NANO_TO_MICRO = 1000L;

	private static ThreadLocal<TracerLogger> context = new ThreadLocal<>();
	
	private static final Logger LOG = LoggerFactory.getLogger(Tracer.class);
    
	/**
	 * Constructor.
	 */
    private Tracer() { }
    
   /**
    * Create a new instance of a Tracer.
    */
    public static void create() {
        context.set(new TracerLogger());
    }

   /**
    * Destroy the Tracer instance.
    */
    public static void destroy() {
        context.set(null);
    }
    
    public static boolean isTracerRunning() {
    	return context.get() != null;
    }
    
	public static void mark(String s) {
		get().mark(s);
	}

	public static void markAndOutput(String s) {
		get().markAndOutput(s);
	}
	
	public static void markAndOutputInMicros(String s) {
		get().markAndOutputInMicros(s);
	}

	public static void markInMicros(String s) {
		get().markInMicros(s);
	}

	public static void output() {
		get().output();
	}

	public static void outputInMicros() {
		get().outputInMicros();
	}

	public static void reset() {
		get().reset();
	}
	
	public static void setId(String id) {
		get().setId(id);
	}
	
    private static TracerLogger get() {
    	TracerLogger t = context.get();
    	if (t == null) {
    		create();
    		t = context.get();
    	}
    	return t;
    }
    
    /**
     * Handle logging formatting for tracer.
     * @author nphillips
     */
    private static final class TracerLogger {
   
        private static final String LINE_FEED = System.getProperty("line.separator");
        private static final String STMT_TRACE = LINE_FEED + "----- TRACE -----" + LINE_FEED;
    	
    	private String id;
    	private Map<String, Long> map = new LinkedHashMap<>();
    	
    	/**
    	 * Set the tracer ID.
    	 * @param id to use
    	 */
    	public void setId(String id) {
    		this.id = id;
    	}
    	
    	/**
    	 * Reset tracer, clear markers.
    	 */
        public void reset() {
            map.clear();
        }
        
       /**
        * 
        * @param s  Marker name
        */ 
        public void mark(String s) {
        	if (map.get(s) == null) {
        		map.put(s, System.currentTimeMillis());
//        	} else if (!warnMessagesDisabled) {
//        		Logger.warn(this, "You must not call Tracer.mark() with the same marker more than once '" + s + "'");
        	}
        }
        
        /**
         * 
         * @param s  Marker name
         */
        public void markInMicros(String s) {
            map.put(s, System.nanoTime());
        }
        
        /**
         * 
         * @param s  Marker name
         */
        public void markAndOutput(String s) {
            mark(s);
            output();
        }
        
        /**
         * 
         * @param s  Marker name
         */
        public void markAndOutputInMicros(String s) {
            markInMicros(s);
            outputInMicros();
        }
        
        /**
         * Output trace in milliseconds.
         */
        public void output() {
            long prev = 0;
            long total = 0;
            StringBuilder sb = new StringBuilder();
            
            if (id == null) {
            	sb.append(STMT_TRACE);
            } else {
            	sb.append(LINE_FEED).append("----- ").append(id);
                sb.append(" -----").append(LINE_FEED);
            }
            for (String s : map.keySet()) {
                long amount = map.get(s);
                long time = prev == 0 ? 0 : (amount - prev);
                total += time;
                prev = amount;
                detailLine(sb, s, "millis", time, total);
            }
            LOG.info(sb.toString());
            reset();
        }
        
        /**
         * Output trace in microseconds.
         */
        public void outputInMicros() {
            long prev = 0;
            long total = 0;
            StringBuilder sb = new StringBuilder();
            
            if (id == null) {
            	sb.append(STMT_TRACE);
            } else {
            	sb.append(LINE_FEED).append("----- ").append(id);
                sb.append(" -----").append(LINE_FEED);
            }
            for (String s : map.keySet()) {
                long amount = map.get(s);
                long time = prev == 0 ? 0 : (amount - prev);
                total += time;
                prev = amount;
                detailLine(sb, s, "micros", nanoToMicro(time), nanoToMicro(total));
            }
            LOG.info(sb.toString());
            reset();
        }
        
        /**
         * Format the tracer detail line.
         * @param sb result
         * @param marker name
         * @param units units
         * @param time elapsed
         * @param total elapsed
         */
        private void detailLine(StringBuilder sb, String marker, String units, long time, long total) {
            sb.append("  ").append(marker).append(" ").append(time).append(" ").append(units).
            	append(". [").append(total);
            sb.append(" ").append(units).append(".]").append(LINE_FEED);
        }
        
        /**
         * Convert measures to microseconds.
         * @param nanoseconds measurements
         * @return in microseconds
         */
        private long nanoToMicro(long nanoseconds) {
            return nanoseconds / NANO_TO_MICRO;
        }
    	
    }

}