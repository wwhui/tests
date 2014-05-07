package com.sdhz.dao;

import java.util.List;
import java.util.Map;

import com.sdhz.domain.AnswerResult;
import com.sdhz.domain.OptionAnswer;
import com.sdhz.domain.Question;
import com.sdhz.domain.Subject;

public interface AnswerDao {
  public long addQeustion(Question question);
  public long addOptionAnswer(OptionAnswer optionAnswer);
  public long addsubject(Subject subject);
  public List<Map<String, Object>> getRandomQuestion(String id,String num);
  public void addFreeQuestion();
  public List<Map<String, Object>> getSubject();
  public long addScore(AnswerResult aResult,String isup);
  public List<Map<String, Object>> getHistoryResult();
  public void deleteAll();
  public void deleteScore();
  public void deleteOneScore(String id);
}
