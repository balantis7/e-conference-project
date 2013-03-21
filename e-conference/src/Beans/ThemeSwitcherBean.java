package Beans;  
    
import java.util.Map;  
import java.util.TreeMap;  
import javax.annotation.PostConstruct;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped  

public class ThemeSwitcherBean {  
          
    private Map<String, String> themes;  
       
      
    private String theme;  
      
    private String gp;  
  
    public void setGp(String gp) {  
        this.gp = gp;  
    }  
      
    public Map<String, String> getThemes() {  
        return themes;  
    }  
  
    public String getTheme() {  
        return theme;  
    }  
  
    public void setTheme(String theme) {  
        this.theme = theme;  
    }  
  
    @PostConstruct  
    public void init() {  
          
         
          
        themes = new TreeMap<String, String>();  
        themes.put("Aristo", "aristo");   
        themes.put("Bluesky", "bluesky");  
        themes.put("Casablanca", "casablanca");  
        themes.put("Cupertino", "cupertino");    
        themes.put("Eggplant", "eggplant");    
        themes.put("Flick", "flick");  
        themes.put("Glass-X", "glass-x");   
        themes.put("Smoothness", "smoothness");  
    }  
      
    public void saveTheme() {  
        gp=theme;  
    }  
   
}  