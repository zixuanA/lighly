package com.mredrock.cyxbs.freshman.Bean;

import java.util.List;

public class MessageBean {
    /**
     * code : 200
     * message : 成功!
     * result : [{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=1382778973","pic":"http://p2.music.126.net/6CB6Jsmb7k7qiJqfMY5Row==/109951164260234943.jpg?param=300x300","type":"netease","title":"You Need To Calm Down","lrc":"","songid":1382778973,"url":"http://music.163.com/song/media/outer/url?id=1382778973.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=19292984","pic":"http://p2.music.126.net/KurKrZ-dMmviArT5lM2RCQ==/18517974836953202.jpg?param=300x300","type":"netease","title":"Love Story","lrc":"","songid":19292984,"url":"http://music.163.com/song/media/outer/url?id=19292984.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=19292800","pic":"http://p2.music.126.net/L8urLE0rTu9gWeM1DM2bNg==/6621259023379377.jpg?param=300x300","type":"netease","title":"Red","lrc":"","songid":19292800,"url":"http://music.163.com/song/media/outer/url?id=19292800.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=19292987","pic":"http://p2.music.126.net/KurKrZ-dMmviArT5lM2RCQ==/18517974836953202.jpg?param=300x300","type":"netease","title":"You Belong With Me","lrc":"","songid":19292987,"url":"http://music.163.com/song/media/outer/url?id=19292987.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=33337002","pic":"http://p2.music.126.net/JI13dWBRcSzXDQEJeOLunA==/7804333535025554.jpg?param=300x300","type":"netease","title":"Welcome To New York (Karaoke Version)","lrc":"","songid":33337002,"url":"http://music.163.com/song/media/outer/url?id=33337002.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=25648017","pic":"http://p2.music.126.net/QtHTu9JysVWTat30lFrXTA==/109951163208497947.jpg?param=300x300","type":"netease","title":"I Knew You Were Trouble.","lrc":"","songid":25648017,"url":"http://music.163.com/song/media/outer/url?id=25648017.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=29713617","pic":"http://p2.music.126.net/ap7-LShvjYmzPJ6xR3XG5Q==/3231464674386227.jpg?param=300x300","type":"netease","title":"Blank Space","lrc":"","songid":29713617,"url":"http://music.163.com/song/media/outer/url?id=29713617.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=19292838","pic":"http://p2.music.126.net/GkKqLo7rY-Uxs-415At1xg==/109951163008406494.jpg?param=300x300","type":"netease","title":"Sparks Fly","lrc":"","songid":19292838,"url":"http://music.163.com/song/media/outer/url?id=19292838.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=28953033","pic":"http://p2.music.126.net/yGjQrAKIGlO--FJZ5jdOOg==/109951163071997318.jpg?param=300x300","type":"netease","title":"Shake It Off","lrc":"","songid":28953033,"url":"http://music.163.com/song/media/outer/url?id=28953033.mp3"},{"author":"Taylor Swift","link":"http://music.163.com/#/song?id=19292852","pic":"http://p2.music.126.net/GkKqLo7rY-Uxs-415At1xg==/109951163008406494.jpg?param=300x300","type":"netease","title":"Enchanted","lrc":"","songid":19292852,"url":"http://music.163.com/song/media/outer/url?id=19292852.mp3"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * author : Taylor Swift
         * link : http://music.163.com/#/song?id=1382778973
         * pic : http://p2.music.126.net/6CB6Jsmb7k7qiJqfMY5Row==/109951164260234943.jpg?param=300x300
         * type : netease
         * title : You Need To Calm Down
         * lrc :
         * songid : 1382778973
         * url : http://music.163.com/song/media/outer/url?id=1382778973.mp3
         */

        private String author;
        private String link;
        private String pic;
        private String type;
        private String title;
        private String lrc;
        private int songid;
        private String url;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public int getSongid() {
            return songid;
        }

        public void setSongid(int songid) {
            this.songid = songid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
