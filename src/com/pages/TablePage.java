/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pages;

import com.*;
import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import com.sun.lwuit.Display;


import java.util.Vector;

import com.components.HTMLComponentItem;
import com.components.LinkButton;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author caxthelm
 */
public class TablePage extends BasePage {
    //Common Command Ids ;
    private final int COMMAND_BACK = COMMAND_RIGHT;
    private final int COMMAND_ABOUT = 25;
    
    //Lwuit Commands:   
    Component m_cTable = null;
    String m_sTableText = "";
    Timer m_tiReloadTimer = null;
    
    
    private class SetPageTimerTask extends TimerTask {
        public SetPageTimerTask() {
        }//send SetPageTimerTask()
        public void run() {
            if(m_cTable != null) {
                HTMLParser.resetAllTable(m_cTable);
                m_cForm.repaint();
                Thread.yield();
            }
        }//end run()
    }
    
    public TablePage(String _sTitle, String _sTableText) {
        super("TablePageForm", PAGE_TABLE);
        
        if(!m_bIsLoaded) {
            //TODO: make error dialog.
            System.err.println("We failed to load");
            return;
        }
        Label titleLabel = (Label)mainMIDlet.getBuilder().findByName("TitleLabel", m_cHeaderContainer);
        if(titleLabel != null) {
            //titleLabel.setText(_sTitle);
        }
        m_sTableText = _sTableText;
        try {
            //Create dynamic components here.
            m_cForm.addShowListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    m_cForm.removeShowListener(this);
                    //NetworkController.showLoadingDialog();
                    addData(m_sTableText, -1);
                    //NetworkController.hideLoadingDialog();
                }
            });
            updateSoftkeys();
            m_cForm.addCommandListener(this);
            //mForm.repaint();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }//end SearchPage()
    
    public void updateSoftkeys() {
        int i = 0;
        m_cForm.removeAllCommands();
        String  str = "";
        str = mainMIDlet.getString("BackSK");
        m_cForm.addCommand(new Command(str, COMMAND_BACK), i++);
    }//end updateSoftkeys()
    
    public void actionPerformed(ActionEvent ae) {
        System.err.println("Action TablePage: " + ae.getCommand().getId());
        int commandId = ae.getCommand().getId();
        if(commandId == COMMAND_OK) {
            Component focusedComp = m_cForm.getFocused();
            if(focusedComp instanceof Button){
                Button test = (Button)focusedComp;
                commandId = test.getCommand().getId();
            }else if(focusedComp instanceof Container) {
                Container testCont = (Container)focusedComp;
                Button test = (Button)testCont.getLeadComponent();
                if(test != null && testCont.getLeadComponent() instanceof Button) {
                    commandId = test.getCommand().getId();
                }
            }
        }
        
        switch(commandId) {
            //Softkeys
            case COMMAND_BACK:
                mainMIDlet.pageBack();
                break;
                
            case COMMAND_IMAGE:
                {
                    Component oComp = ae.getComponent();
                    if(oComp instanceof LinkButton) {
                        String url = "http:"+((LinkButton)oComp).getLink();
                        System.out.println("url: "+url);
                        mainMIDlet.setCurrentPage(new ImageDialog(
                                ((LinkButton)oComp).getOtherInfo(), ((LinkButton)oComp).getText(), url));
                    }
                }
                break;
            case COMMAND_LINK: //internal links
                {
                    Component oComp = ae.getComponent();
                    if(oComp instanceof LinkButton) {
                        String url = ((LinkButton)oComp).getLink();
                        int wikiIdx = url.indexOf("/wiki/");
                        if(wikiIdx >= 0) {
                            
                            mainMIDlet.pageBack();
                            String title = url.substring(wikiIdx + 6);
                            //System.out.println("linkTitle: "+title);
                            NetworkController.getInstance().fetchArticle(mainMIDlet.getLanguage(), title,  "0");
                        }
                    }
                }
                break;
        }
    } //end actionPerformed(ActionEvent ae)
    
    public void refreshPage() {        
        m_cForm.addShowListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //Putting the refresh in a show listener to make sure the page is ready to refresh.
                m_cForm.removeShowListener(this);
                checkRefresh();
            }
        });
    }//end refreshPage()
    
    private void checkRefresh() {
        NetworkController.hideLoadingDialog();
        addData(null, NetworkController.PARSE_SEARCH);
        Thread.yield();
        
        super.refreshPage();
    }//end checkRefresh()
    
    public void addData(Object _results, int _iResultType) {
        if(_results == null || !(_results instanceof String)) {
            return;
        }
        if(m_cContentContainer != null )
        {              
            m_cTable = HTMLParser.parseHtml((String)_results, true);      
            m_cTable.setUIID("Table");
            if(m_cTable instanceof Container) {
                ((Container)m_cTable).invalidate();
                //((Container)cTextComp).revalidate();
            }
            m_cContentContainer.addComponent(m_cTable);
            //HTMLParser.resetAll(m_cTable);
        }//end if(m_cContentContainer != null && sections != null && sections.size() > 0)
        m_cForm.invalidate();
        m_cContentContainer.invalidate();
        m_cForm.repaint();
        Thread.yield();
        m_tiReloadTimer = new Timer();
        m_tiReloadTimer.schedule(new TablePage.SetPageTimerTask(), 1000);
    }//end addData(Object _results)
    
    
}
