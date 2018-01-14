package com.zhuanghongji.volley.sample;

/**
 * Created by zhuanghongji on 2018/1/14.
 */

public class Weather {

    private WeatherInfo weatherInfo;

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherInfo=" + weatherInfo +
                '}';
    }

    private class WeatherInfo {

        private String city;
        private String cityId;
        private String temp;
        private String wd;
        private String ws;
        private String sd;
        private String wse;
        private String time;
        private String isRadar;
        private String radar;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWd() {
            return wd;
        }

        public void setWd(String wd) {
            this.wd = wd;
        }

        public String getWs() {
            return ws;
        }

        public void setWs(String ws) {
            this.ws = ws;
        }

        public String getSd() {
            return sd;
        }

        public void setSd(String sd) {
            this.sd = sd;
        }

        public String getWse() {
            return wse;
        }

        public void setWse(String wse) {
            this.wse = wse;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIsRadar() {
            return isRadar;
        }

        public void setIsRadar(String isRadar) {
            this.isRadar = isRadar;
        }

        public String getRadar() {
            return radar;
        }

        public void setRadar(String radar) {
            this.radar = radar;
        }

        @Override
        public String toString() {
            return "WeatherInfo{" +
                    "city='" + city + '\'' +
                    ", cityId='" + cityId + '\'' +
                    ", temp='" + temp + '\'' +
                    ", wd='" + wd + '\'' +
                    ", ws='" + ws + '\'' +
                    ", sd='" + sd + '\'' +
                    ", wse='" + wse + '\'' +
                    ", time='" + time + '\'' +
                    ", isRadar='" + isRadar + '\'' +
                    ", radar='" + radar + '\'' +
                    '}';
        }
    }
}
