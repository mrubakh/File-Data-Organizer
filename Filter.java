import java.util.ArrayList;


public class Filter{
  
  public enum FilterAction{SORT_BY_ASCENDING, SORT_BY_DESCENDING, INCLUDE_IF, EXCLUDE_IF};
  public enum FilterType{STARTS_WITH, ENDS_WITH, CONTAINS, EQUALS};
  
  private FilterAction action;
  private FilterType type;
  private String field, value;
  
  public Filter(FilterAction a, String f){
    this(a, f, null, null);
  }
  
  
  public Filter(FilterAction a, String f, FilterType ft, String v){
    if ((a == FilterAction.INCLUDE_IF || a==FilterAction.EXCLUDE_IF) && (ft ==null || v == null))
      throw new IllegalArgumentException("No filter type or value provided for non-sort filter!");
    action = a;
    field = f;
    type = ft;
    value = v;
  }
  
  
  public boolean isSort(){
    return (action == FilterAction.SORT_BY_ASCENDING || action == FilterAction.SORT_BY_DESCENDING);
  }
  
  
  public FilterAction getAction(){
    return action; 
  }
  
  public FilterType getFilterType(){
    return type;
  }
  
  public String getField(){
    return field;
  }
  
  public String getValue(){
    return value;
  }
    
  public String toString(){
    if (isSort())
      return action + " " + field;
    return action + " " + field + " " + type + " " + value;
      
  }
    
  
  
}