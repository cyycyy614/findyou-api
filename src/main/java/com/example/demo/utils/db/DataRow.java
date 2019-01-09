package com.example.demo.utils.db;

public class DataRow {
    Object[] obj;
    public DataRow(Object[] obj){
        this.obj = obj;
    }

    public int size(){
        if(obj == null){
            return 0;
        }
        return obj.length;
    }
    public int getInt(int index){
        if(obj == null){
            return 0;
        }
        String val = obj[index].toString();
        return Integer.parseInt(val);
    }

    public String getString(int index){
        if(obj == null){
            return "";
        }
        return obj[index].toString();
    }

    public boolean getBoolean(int index){
        if(obj == null){
            return false;
        }
        String val = obj[index].toString();
        return Boolean.parseBoolean(val);
    }

    public double getDouble(int index){
        if(obj == null){
            return 0;
        }
        String val = obj[index].toString();
        return Double.parseDouble(val);
    }
}
