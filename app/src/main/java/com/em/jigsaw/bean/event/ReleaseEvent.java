package com.em.jigsaw.bean.event;

/**
 * Time ： 2018/12/29 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ReleaseEvent {
    private int Event;//0.发布成功 1.发布失败

    public ReleaseEvent(int event) {
        Event = event;
    }

    public int getEvent() {
        return Event;
    }

    public void setEvent(int event) {
        Event = event;
    }
}
