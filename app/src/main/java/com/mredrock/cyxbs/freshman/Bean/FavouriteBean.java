package com.mredrock.cyxbs.freshman.Bean;

import java.util.List;

public class FavouriteBean {
    private List<FavouriteItemBean> list;
    private List<AlbumBean> albumList;

    public List<AlbumBean> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<AlbumBean> albumList) {
        this.albumList = albumList;
    }

    public List<FavouriteItemBean> getList() {
        return list;
    }

    public void setList(List<FavouriteItemBean> list) {
        this.list = list;
    }

    public static class FavouriteItemBean{
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        private String url;
        private String title;
        private String detail;
        private List<MessageBean.ResultBean> songList;

        public List<MessageBean.ResultBean> getSongList() {
            return songList;
        }

        public void setSongList(List<MessageBean.ResultBean> songList) {
            this.songList = songList;
        }
        public void addSong(MessageBean.ResultBean song){
            this.songList.add(song);
        }
        public void removeSong(int index){
            this.songList.remove(index);
        }
    }
    public static class AlbumBean{
        private String albumName;

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }
    }
}
