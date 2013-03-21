package language;

import java.io.*;
import java.util.*;

import javax.faces.bean.*;
import javax.faces.context.*;
import javax.faces.event.*;

@ManagedBean
@SessionScoped
public class setLanguage implements Serializable{
	private boolean isEnglish = true;
	private final Locale ENGLISH = Locale.ENGLISH;
	private final Locale GREEK = new Locale("el");
	
	public void swapLocale(ActionEvent event) {
	    switchLocale();
	  }
	  
	  private void switchLocale() {
	    isEnglish = !isEnglish;
	    Locale newLocale;
	    if (isEnglish) {
	      newLocale = ENGLISH;
	    } else {
	      newLocale = GREEK;
	    }
	    FacesContext.getCurrentInstance().getViewRoot().setLocale(newLocale);
	  }

	  public Locale getLocale() {
	    if (isEnglish) {
	      return(ENGLISH);
	    } else {
	      return(GREEK);
	    }
	  }
	  
	  public boolean isChecked() {
		    return(false);
		  }
		  
	  public void setChecked(boolean flag) {}

}
