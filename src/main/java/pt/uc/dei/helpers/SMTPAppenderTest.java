package pt.uc.dei.helpers;

import org.apache.log4j.spi.LoggingEvent; 
import org.apache.log4j.spi.TriggeringEventEvaluator; 
 
/**
 * Tests for SMTPAppender. 
 */ 
public class SMTPAppenderTest  implements TriggeringEventEvaluator { 

    public boolean isTriggeringEvent(LoggingEvent event) { 
    	
    	return true; } 

    }
