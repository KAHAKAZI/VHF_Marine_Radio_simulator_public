package org.vhfradio.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.primefaces.PrimeFaces;
@ManagedBean
@ViewScoped
public class RadioManagedBean implements Serializable {
    private boolean outputBoardForm;
    private boolean slideMenuForm;
    private boolean distressForm;
    private boolean dscMessageForm;
    private boolean dscAckMessageForm;
    private boolean mmsiForm;
    private boolean gpsForm;
    private boolean individualCallForm;
    private boolean dscIndCallForm;
    private String distressMessage;
    private final String latitude = "12*02.2N";
    private final String longitude = "032*33.4W";
    private int individualCallMmsi = 000000000;

    @Pattern(regexp = "^(0?[1-9]|1[0-9]|2[0-8]|6[0-9]|7[1-9]|8[0-8])$", message = "Channel must be between 1-28 or 60-69 or 71-88")
    @Size(min = 1, max = 2, message = "Channel must be 1 or 2-digits number")
    private String individualCallChannel = "0";

    @PostConstruct
    public void init() {
        showOutboardForm();
    }

    public boolean getSlideMenuForm() {
        return slideMenuForm;
    }

    public void setSlideMenuForm(boolean slideMenu) {
        this.slideMenuForm = slideMenu;
    }

    public boolean getOutputBoardForm() {
        return outputBoardForm;
    }

    public void setOutputBoardForm(boolean outputBoard) {
        this.outputBoardForm = outputBoard;
    }

    public boolean getDistressForm() {
        return distressForm;
    }

    public void setDistressForm(boolean distress) {
        this.distressForm = distress;
    }

    public String getDistressMessage() {
        return distressMessage;
    }

    public void setDistressMessage(String distressMessage) {
        this.distressMessage = distressMessage;
    }

    public boolean getDscMessageForm() {
        return dscMessageForm;
    }

    public void setDscMessageForm(boolean dscMessageForm) {
        this.dscMessageForm = dscMessageForm;
    }

    public boolean getDscAckMessageForm() {
        return dscAckMessageForm;
    }

    public void setDscAckMessageForm(boolean dscAckMessageForm) {
        this.dscAckMessageForm = dscAckMessageForm;
    }

    public boolean getMmsiForm() {
        return mmsiForm;
    }

    public void setMmsiForm(boolean mmsiForm) {
        this.mmsiForm = mmsiForm;
    }

    public boolean getGpsForm() {
        return gpsForm;
    }

    public void setGpsForm(boolean gpsForm) {
        this.gpsForm = gpsForm;
    }

    public boolean getIndividualCallForm() {
        return individualCallForm;
    }

    public void setIndividualCallForm(boolean individualCallForm) {
        this.individualCallForm = individualCallForm;
    }

    public boolean getDscIndCallForm() {
        return dscIndCallForm;
    }

    public void setDscIndCallForm(boolean dscIndCallForm) {
        this.dscIndCallForm = dscIndCallForm;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getIndividualCallMmsi() {
        return individualCallMmsi;
    }

    public void setIndividualCallMmsi(int individualCallMmsi) {
        this.individualCallMmsi = individualCallMmsi;
    }

    public String getIndividualCallChannel() {
        return individualCallChannel;
    }

    public void setIndividualCallChannel(String individualCallChannel) {
        this.individualCallChannel = individualCallChannel;
    }

    public void recorderStopped() {
        growlMsg("Recording stopped");
    }

    public void recorderStarted() {
        growlMsg("Recording started");
    }

    public void updateMenuForm() {
        if (this.outputBoardForm == true) {
            hideForms();
            this.slideMenuForm = true;
        } else {
            showOutboardForm();
            PrimeFaces.current().executeScript(Scripts.ASS_VAL);
        }
    }

    public void showDistressForm() {
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("action");
        distressMessage = "DISTRESS ";
        if (param != null) {
            distressMessage += param + " ";
        }
        distressMessage += "AT ";
        distressMessage += latitude + ", ";
        distressMessage += longitude + " ";
        hideForms();
        this.distressForm = true;
    }

    public void sendDistress() {
        growlMsg("Distress sent");
        String script = Scripts.SEND_DIST_ + distressMessage + "')";
        PrimeFaces.current().executeScript(script);
        System.out.println(script);
        showOutboardForm();
        PrimeFaces.current().executeScript(Scripts.REQ_CH_16);
    }

    public void sendIndividualCall() {
        String script = Scripts.SEND_IND_CALL_ + individualCallMmsi + ", " + individualCallChannel + ");";
        PrimeFaces.current().executeScript(script);
        growlMsg("Individual Call sent");
        showOutboardForm();
        PrimeFaces.current().executeScript(Scripts.ASS_VAL);
        this.individualCallMmsi = 0;
        this.individualCallChannel = "0";
    }

    public void sendIndividualCallResponse() {
        String response = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("indcallres");
        showOutboardForm();
        if (response.equals("unable")) {
            PrimeFaces.current().executeScript(Scripts.UNABLE_TO_COMP);
            growlMsg("Unable to Comply sent");
        } else if (response.equals("able")) {
            PrimeFaces.current().executeScript(Scripts.ABLE_TO_COMP);
            growlMsg("Able to Comply sent");
        }
    }

    public void sendAcknowledgement() {
        PrimeFaces.current().executeScript(Scripts.SEND_ACK);
        growlMsg("Acknowledgement sent");
        showOutboardForm();
        PrimeFaces.current().executeScript(Scripts.REQ_CH_16);
    }

    public void cancel() {
        showOutboardForm();
        PrimeFaces.current().executeScript(Scripts.ASS_VAL);
    }

    public void showDscMessageForm() {
        hideForms();
        this.dscMessageForm = true;
        PrimeFaces.current().executeScript(Scripts.ASS_DSC_MSG_VAL);
    }

    public void showAckMessageForm() {
        hideForms();
        this.dscAckMessageForm = true;
        PrimeFaces.current().executeScript(Scripts.ASS_DSC_ACK_MSG_VAL);
    }

    public void showMmsiForm() {
        hideForms();
        this.mmsiForm = true;
        PrimeFaces.current().executeScript(Scripts.REQ_MMSI);
    }

    public void showGpsForm() {
        hideForms();
        this.gpsForm = true;
    }

    public void showIndividualCallForm() {
        hideForms();
        this.individualCallForm = true;
    }

    public void showDscIndCallForm() {
        hideForms();
        this.dscIndCallForm = true;
        PrimeFaces.current().executeScript(Scripts.ASS_DSC_INDCALL_VAL);
    }

    private void growlMsg(String msg) {
        FacesMessage message = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void showOutboardForm() {
        hideForms();
        this.outputBoardForm = true;
    }
    
    private void hideForms(){
        this.outputBoardForm = false;
        this.slideMenuForm = false;
        this.distressForm = false;
        this.dscMessageForm = false;
        this.dscAckMessageForm = false;
        this.mmsiForm = false;
        this.gpsForm = false;
        this.individualCallForm = false;
        this.dscIndCallForm = false;
    }
}
