package org.web.vexamine.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportEntity {

    private String mailId; 
    
    private String questionBankName;
    
    private String category;
    
    private String testDate; 
    
    private long questionTaken;
    
    private long answeredRight;
    
    private long answeredWrong;
    
    private long rightanswer;
    
    private long wronganswer;
    
    private long unanswer;
    
    private String timeTaken;
    
    private String percent; 
    
    private String rightLabel; 
    
    private String wrongLabel; 

}
