package org.dragon.rmm.domain.message;

/**
 * 通知消息类型
 * 
 * @author dengjie
 * 
 * @version 1.0 2012-11-28
 * @since 1.0
 * 
 */
public enum NotificationType {
    /**
     * 评论通知 (1)
     */
    COMMENT(1),

    /**
     * 蜜告(2)
     */
    MIGAO(2),
    /**
     * 被迫强制退出(3)
     */
    FORCE_QUIT(3);

    private int code;

    /**
     * 定义码值
     * 
     * @param i
     *            类型码
     */
    private NotificationType(int code) {
        this.code = code;
    }

    /**
     * 获取类型码
     * 
     * @return 类型码
     */
    public int code() {
        return code;
    }
}
