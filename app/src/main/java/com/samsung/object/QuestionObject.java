package com.samsung.object;

/**
 * Created by SamSunger on 5/16/2015.
 */
public class QuestionObject {

    private String PK_ID;
    private String DISENO_ID;
    private String POSICION_COLUMA;
    private String NOMBRE_COLUMNA;
    private String TEXTO_PREGUNTA;
    private String Q_TYPE = "QType";//7
    private String QuestionType;
    private String QuestionTypeDescription;
    private String QuestionAnswer;
    private int selected;
    private String GRUPO_RESPUESTAS_ID;


    public QuestionObject(String PK_ID, String DISENO_ID, String POSICION_COLUMA, String NOMBRE_COLUMNA, String TEXTO_PREGUNTA, String q_TYPE, String questionType, String questionTypeDescription, String questionAnswer, int selected, String GRUPO_RESPUESTAS_ID) {
        this.PK_ID = PK_ID;
        this.DISENO_ID = DISENO_ID;
        this.POSICION_COLUMA = POSICION_COLUMA;
        this.NOMBRE_COLUMNA = NOMBRE_COLUMNA;
        this.TEXTO_PREGUNTA = TEXTO_PREGUNTA;
        Q_TYPE = q_TYPE;
        QuestionType = questionType;
        QuestionTypeDescription = questionTypeDescription;
        QuestionAnswer = questionAnswer;
        this.selected = selected;
        this.GRUPO_RESPUESTAS_ID = GRUPO_RESPUESTAS_ID;
    }

    public String getPK_ID() {
        return PK_ID;
    }

    public void setPK_ID(String PK_ID) {
        this.PK_ID = PK_ID;
    }

    public String getDISENO_ID() {
        return DISENO_ID;
    }

    public void setDISENO_ID(String DISENO_ID) {
        this.DISENO_ID = DISENO_ID;
    }

    public String getPOSICION_COLUMA() {
        return POSICION_COLUMA;
    }

    public void setPOSICION_COLUMA(String POSICION_COLUMA) {
        this.POSICION_COLUMA = POSICION_COLUMA;
    }

    public String getNOMBRE_COLUMNA() {
        return NOMBRE_COLUMNA;
    }

    public void setNOMBRE_COLUMNA(String NOMBRE_COLUMNA) {
        this.NOMBRE_COLUMNA = NOMBRE_COLUMNA;
    }

    public String getTEXTO_PREGUNTA() {
        return TEXTO_PREGUNTA;
    }

    public void setTEXTO_PREGUNTA(String TEXTO_PREGUNTA) {
        this.TEXTO_PREGUNTA = TEXTO_PREGUNTA;
    }

    public String getQ_TYPE() {
        return Q_TYPE;
    }

    public void setQ_TYPE(String q_TYPE) {
        Q_TYPE = q_TYPE;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getQuestionTypeDescription() {
        return QuestionTypeDescription;
    }

    public void setQuestionTypeDescription(String questionTypeDescription) {
        QuestionTypeDescription = questionTypeDescription;
    }

    public String getQuestionAnswer() {
        return QuestionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        QuestionAnswer = questionAnswer;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getGRUPO_RESPUESTAS_ID() {
        return GRUPO_RESPUESTAS_ID;
    }

    public void setGRUPO_RESPUESTAS_ID(String GRUPO_RESPUESTAS_ID) {
        this.GRUPO_RESPUESTAS_ID = GRUPO_RESPUESTAS_ID;
    }
}
