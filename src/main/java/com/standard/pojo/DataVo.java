package com.standard.pojo;

import org.springframework.boot.autoconfigure.jdbc.DataSourceSchemaCreatedEvent;

/**
 * @author: zhangH
 * @date: 2019/6/1 23:05
 * @description:
 */
public class DataVO {

    public static DataVO ok(Object object){
        DataVO dataVO = new DataVO();
        dataVO.setCode(20000);
        dataVO.setData(object);
        return  dataVO;
    }

    private int code;

    private String message;

    private Object data;

    private int total;

    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
