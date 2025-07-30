package util;

public class Event {


    private int index;
    private String task;
    private Date date;
    private boolean status;


    public Event(int index,String task,Date date,boolean status){
        this.index = index;
        this.task = task;
        this.date = date;
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



    @Override
    public String toString(){
        String finalStatus;
        if(this.status){
            finalStatus = "Completed";
        }else{
            finalStatus = "Pending";
        }
        return String.format("%6d\t%s\t%s : \t\t%s",this.index,this.date,finalStatus,this.task);
    }
}
