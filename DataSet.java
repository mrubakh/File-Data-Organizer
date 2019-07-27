import java.util.ArrayList;
import java.util.*;

public class DataSet{
  
  private int indexInField;
  private boolean flag;
  
  //we're saying comparator<String> because we know all forms of data were comparing will be strings, 
  //even if its "21" its still treated as a string in the ssd file
  public class OurComparator implements Comparator<String[]>{
    
    //use our indexinField to determine which element of data to compare
    
    public int compare(String[] a, String[] b){
     if(!flag)
     return b[indexInField].compareTo(a[indexInField]);
     
     
     return a[indexInField].compareTo(b[indexInField]);
    }
    
  }
  
  private String[] fieldNames;
  private ArrayList<String[]> data;
  
  public DataSet(String[] fieldNames, ArrayList<String[]> data)
  {
    
    this.fieldNames = fieldNames;
    this.data = data;
    
    //Debug code -- prints in the passed in column headers and first row of Data
    //Comment out or remove this in your final code!
  //System.out.println(Arrays.toString(fieldNames));
   // System.out.println(Arrays.toString(data.get(0)));
    
  }
  
  
  
  //RETURN A NEW STRING OF DATA DONT MODIFY THE OLD ONE **********
  public ArrayList<String[]> applyFilters(ArrayList<Filter> filters){
    
    //making a new array list of string[] so we dont destroy our original data, and can filter it again after this 
     ArrayList<String[]> result = new ArrayList<String[]>();
  //filling our result array with the same set as the data array, so we can remove from the list without deleting permanently from list
    for(int i = 0; i < data.size(); i++){
      result.add(i, data.get(i));      
    }
    
    
    for(int x = 0; x < filters.size(); x ++){
      
      
      //Check 
      if(filters.get(x).getAction() == Filter.FilterAction.INCLUDE_IF){
        
        Filter currentFilter = filters.get(x);
        //to make sure we ignore letter case
        String fieldInUse = currentFilter.getField().toUpperCase();
        
        indexInField = 0;
        for(int i = 0; i < fieldNames.length; i++){
         
          if(fieldNames[i].toUpperCase().equals(fieldInUse)){
            indexInField = i;
            break;
          }
          
        }
      //  System.out.println("Index in field: " + indexInField);
        
        for(int j = result.size()-1; j >= 0; j--){
     // System.out.println(currentFilter.getValue().length());
     // System.out.println(result.get(j)[indexInField].length());
          if((currentFilter.getValue().length() > result.get(j)[indexInField].length()) || (currentFilter.getFilterType() == Filter.FilterType.STARTS_WITH
             && !(result.get(j)[indexInField].substring(0, currentFilter.getValue().length()).toUpperCase().equals(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
          else if(currentFilter.getFilterType() == Filter.FilterType.ENDS_WITH 
           && (!(result.get(j)[indexInField].substring(result.get(j)[indexInField].length()-currentFilter.getValue().length()).toUpperCase().equals(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
          else if(currentFilter.getFilterType() == Filter.FilterType.CONTAINS 
           && (!(result.get(j)[indexInField].toUpperCase().contains(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
          else if(currentFilter.getFilterType() == Filter.FilterType.EQUALS
           && (!(result.get(j)[indexInField].toUpperCase().equals(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
         
        }

        
      }
      
      
      if(filters.get(x).getAction() == Filter.FilterAction.EXCLUDE_IF){
        
        Filter currentFilter = filters.get(x);
        //to make sure we ignore letter case
        String fieldInUse = currentFilter.getField().toUpperCase();
        
        indexInField = 0;
        for(int i = 0; i < fieldNames.length; i++){
         
          if(fieldNames[i].toUpperCase().equals(fieldInUse)){
            indexInField = i;
            break;
          }
          
        }
      //  System.out.println("Index in field: " + indexInField);
        
        for(int j = result.size()-1; j >=0; j--){
      //  System.out.println("in loop");
          if((currentFilter.getValue().length() < result.get(j)[indexInField].length()) && (currentFilter.getFilterType() == Filter.FilterType.STARTS_WITH
             && ((result.get(j)[indexInField].substring(0, currentFilter.getValue().length()).toUpperCase().equals(currentFilter.getValue()))))){
            result.remove(result.get(j));
           }
          else if((currentFilter.getValue().length() < result.get(j)[indexInField].length()) && (currentFilter.getFilterType() == Filter.FilterType.ENDS_WITH 
           && ((result.get(j)[indexInField].substring(result.get(j)[indexInField].length()-currentFilter.getValue().length()).toUpperCase().equals(currentFilter.getValue()))))){
            result.remove(result.get(j));
           }
          else if(currentFilter.getFilterType() == Filter.FilterType.CONTAINS 
           && ((result.get(j)[indexInField].toUpperCase().contains(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
          else if(currentFilter.getFilterType() == Filter.FilterType.EQUALS
           && ((result.get(j)[indexInField].toUpperCase().equals(currentFilter.getValue())))){
            result.remove(result.get(j));
           }
        }
        
      }
      if(filters.get(x).getAction() == Filter.FilterAction.SORT_BY_ASCENDING){
        flag = true;
         Filter currentFilter = filters.get(x);
        //to make sure we ignore letter case
        String fieldInUse = currentFilter.getField().toUpperCase();
        
        indexInField = 0;
        for(int i = 0; i < fieldNames.length; i++){
         
          if(fieldNames[i].toUpperCase().equals(fieldInUse)){
            indexInField = i;
            break;
          }
        }
        
       result.sort(new OurComparator()); 
      }
      
      
      if(filters.get(x).getAction() == Filter.FilterAction.SORT_BY_DESCENDING){
        flag = false;
        
         Filter currentFilter = filters.get(x);
        //to make sure we ignore letter case
        String fieldInUse = currentFilter.getField().toUpperCase();
        
        indexInField = 0;
        for(int i = 0; i < fieldNames.length; i++){
         
          if(fieldNames[i].toUpperCase().equals(fieldInUse)){
            indexInField = i;
            break;
          }
        }
        
       result.sort(new OurComparator()); 
      }
      
      
    }
    
    
    //Prints the filters -- here for debugging, c
    //Comment out or remove this in your final code!
   // for (int i = 0; i < filters.size(); i++)
    //  System.out.println("Filter #" + (i+1) + ": " + filters.get(i));  
    
    

    return result;  //placeholder
  }  
  
  
}