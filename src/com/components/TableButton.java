/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.components;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;

/**
 *
 * @author caxthelm
 */
public class TableButton extends Container {
    
    String m_sOtherInfo;
    String m_sLink;
    String m_sText;
        
    public TableButton() {
        super();
        
    }//end LinkButton()
    
    public TableButton(String _sText, String _sLink) {
        super();
        m_sText = _sText;
        m_sLink = _sLink;
        setWrappedText(_sText);
        super.setUIID("ImageButton");
    }//end LinkButton(String _sText, String _sLink)
    
    public String getLink() {
        return m_sLink;
    }//end getLink() 
    
    public String getTitle() {
        return m_sText;
    }//end getTitle()
    
    private void setWrappedText(String _sText) {
        char[] breakItems = {' ', '\n', '\t'};
        boolean isFirst = true;
        while( _sText.length() > 0) {
            String word = "";
            int nextIdx = 0;
            int breakItem = 0;
            for(int i = 0; i < breakItems.length; i++) {
                int idx = _sText.indexOf(breakItems[i]);

                if(nextIdx <= 0 || (idx > 0 && idx < nextIdx)) {
                    nextIdx = idx;
                    breakItem = i;
                }
            }
            String text = "";
            
            //if we broke on a space, keep that space.
            if(breakItems[breakItem] == ' ') {
                nextIdx++;
            }
            if(nextIdx > 0) {
                text += _sText.substring(0, nextIdx);
            }else {
                text += _sText;
            }
            if(isFirst) {
                isFirst = false;
                LinkButton mainButton = new LinkButton("", m_sLink);
                mainButton.setUIID("No_MarginsTransparent");
                mainButton.setCommand(new Command(text, com.pages.BasePage.COMMAND_TABLE));
                mainButton.setOtherInfo(m_sText);
                addComponent(mainButton);
                this.setLeadComponent(mainButton);
            }else {
                Label newLabel = new Label(text);
                newLabel.setUIID("No_MarginsTransparent");
                addComponent(newLabel);
            }
            if(nextIdx == -1) {
                break;
            }
            _sText = _sText.substring(nextIdx);
        }//end while( _sText.length() > 0)
    }//end setWrappedText(String _sText)
}
