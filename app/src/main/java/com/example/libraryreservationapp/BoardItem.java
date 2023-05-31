package com.example.libraryreservationapp;
public class BoardItem {
    private String userId;
    private String title;
    private String content;
    private String boardSeq; // 게시물 번호

    public BoardItem() {
        // 기본 생성자가 필요합니다.
    }

    public BoardItem(String userId, String title, String content, String boardSeq) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.boardSeq = boardSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoardSeq() {
        return boardSeq;
    }

    public void setBoardSeq(String boardSeq) {
        this.boardSeq = boardSeq;
    }
}
