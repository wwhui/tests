package com.sdhz.domain;

import java.io.Serializable;

/** ³é½±»î¶¯ */
public class Lottery implements Serializable
{
    private static final long serialVersionUID = 2761088283064938281L;
    
    private int act_id;
    private String act_name;
    private String start_date;
    private String end_date;
    private int state;
    
    public int getAct_id()
    {
        return act_id;
    }
    public void setAct_id(int act_id)
    {
        this.act_id = act_id;
    }
    public String getAct_name()
    {
        return act_name;
    }
    public void setAct_name(String act_name)
    {
        this.act_name = act_name;
    }
    public String getStart_date()
    {
        return start_date;
    }
    public void setStart_date(String start_date)
    {
        this.start_date = start_date;
    }
    public String getEnd_date()
    {
        return end_date;
    }
    public void setEnd_date(String end_date)
    {
        this.end_date = end_date;
    }
    public int getState()
    {
        return state;
    }
    public void setState(int state)
    {
        this.state = state;
    }
    
    @Override
    public String toString()
    {
        return "Lottery [act_id=" + act_id + ", act_name=" + act_name + ", start_date="
                + start_date + ", end_date=" + end_date + ", state=" + state + "]";
    }

}
