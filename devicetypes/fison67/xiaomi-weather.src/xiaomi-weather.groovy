/**
 *  Xiaomi Sensor Temperature & Humidity (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/

import groovy.json.JsonSlurper
import groovy.transform.Field

@Field 
LANGUAGE_MAP = [
	"atmospheric_pressure":[
    	"Korean": "기압",
        "English": "Atmospheric Pressure"
    ],
    "temperature": [
        "Korean": "온도",
        "English": "Temperature"
    ],
    "humidity": [
        "Korean": "습도",
        "English": "Humidity"
    ],
    "battery": [
    	"Korean": "배터리",
        "English": "Battery"
    ],
    "todays": [
    	"Korean": "오늘",
        "English": "Today's"
    ],
    "high": [
    	"Korean": "최고",
        "English": "High"
    ],
    "low": [
    	"Korean": "최저",
        "English": "Low"
    ],
    "mon": [
    	"Korean": "월",
        "English": "Mon"
    ],
    "tue": [
    	"Korean": "화",
        "English": "Tue"
    ],
    "wed": [
    	"Korean": "수",
        "English": "Wed"
    ],
    "thu": [
    	"Korean": "목",
        "English": "Thu"
    ],
    "fri": [
    	"Korean": "금",
        "English": "Fri"
    ],
    "sat": [
    	"Korean": "토",
        "English": "Sat"
    ],
    "sun": [
    	"Korean": "일",
        "English": "Sun"
    ]
]

metadata {
	definition (name: "xiaomi weather", namespace: "fison67", author: "fison67") {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Battery"
         
        attribute "pressure", "string"
		attribute "maxTemp", "number"
		attribute "minTemp", "number"
		attribute "maxHumidity", "number"
		attribute "minHumidity", "number"
		attribute "multiAttributesReport", "String"
		attribute "currentDay", "String"

        attribute "1l", "string"
        attribute "1t", "string"
        attribute "1h", "string"
        attribute "2l", "string"
        attribute "2t", "string"
        attribute "2h", "string"
        attribute "3l", "string"
        attribute "3t", "string"
        attribute "3h", "string"
        attribute "4l", "string"
        attribute "4t", "string"
        attribute "4h", "string"
        attribute "5l", "string"
        attribute "5t", "string"
        attribute "5h", "string"
        attribute "6l", "string"
        attribute "6t", "string"
        attribute "6h", "string"
        attribute "lastCheckin", "Date"
		attribute "lastCheckinDate", "String"

        command "setLanguage" 
        command "refresh"
	command	"checkNewDay"
	
	}


	simulator {}
	preferences {
		input name: "displayTempHighLow", type: "bool", title: "Display high/low temperature?"
		input name: "displayHumidHighLow", type: "bool", title: "Display high/low humidity?"
		input name: "selectedLang", title:"Select a language" , type: "enum", required: true, options: ["English", "Korean"], defaultValue: "English", description:"Language for DTH"
		input "apiKey", "text", type: "password", title: "Write API Key", description: "thingspeak.com에서 계정을 만들고 채널을 생성하세요.", required: true 
		input "channel", "text", title: "Channel ID", description: "온습도를 기록할 공개 채널을 만들고 채널 ID를 입력해주세요.", required: true
		input name:"tempField", type:"number", title:"Select Temperature Field", description:"Select Temperature Field", required: true
		input name:"humiField", type:"number", title:"Select Humidity Field", description:"Select Humidity Field", required: true
        input "refreshRateMin", "enum", title: "Update interval", defaultValue: 30, options:[5: "5 Min", 10: "10 Min", 15 : "15 Min", 20: "20 Min", 30: "30 Min", ], description: "온습도 업데이트 최소 주기 설정", displayDuringSetup: true        
	}


	tiles(scale: 2) {
        multiAttributeTile(name:"temperature", type:"generic", width:6, height:4) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState("temperature", label:'${currentValue}°',
                    backgroundColors:[
                        // Fahrenheit color set
                        [value: 0, color: "#153591"],
                        [value: 5, color: "#1e9cbb"],
                        [value: 10, color: "#90d2a7"],
                        [value: 15, color: "#44b621"],
                        [value: 20, color: "#f1d801"],
                        [value: 25, color: "#d04e00"],
                        [value: 30, color: "#bc2323"],
                        [value: 44, color: "#1e9cbb"],
                        [value: 59, color: "#90d2a7"],
                        [value: 74, color: "#44b621"],
                        [value: 84, color: "#f1d801"],
                        [value: 95, color: "#d04e00"],
                        [value: 96, color: "#bc2323"]
						// Celsius color set (to switch, delete the 13 lines above anmd remove the two slashes at the beginning of the line below)
                        //[value: 0, color: "#153591"], [value: 7, color: "#1e9cbb"], [value: 15, color: "#90d2a7"], [value: 23, color: "#44b621"], [value: 28, color: "#f1d801"], [value: 35, color: "#d04e00"], [value: 37, color: "#bc2323"]
                    ]
                )
            }
            tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}\n')
            }
            tileAttribute("device.multiAttributesReport", key: "SECONDARY_CONTROL") {
                attributeState("multiAttributesReport", label:'\n${currentValue}' //icon:"st.Weather.weather12",
                ) }
        }        
        valueTile("temperature2", "device.temperature", inactiveLabel: false) {
            state "temperature", label:'${currentValue}°', icon:"https://postfiles.pstatic.net/MjAxODA0MDJfNzkg/MDAxNTIyNjcwOTc4NTIy.9VGDZZ4ieBY5jCJ0tvO8L5HFKbkvnms3ymk62HL4rzMg.HYTGtieTVMLE421M8lF8WE1THRgdyFfb1GG39OhtrU4g.PNG.shin4299/temp.png?type=w3",
            backgroundColors:[
                // Fahrenheit color set
                [value: 0, color: "#153591"],
                [value: 5, color: "#1e9cbb"],
                [value: 10, color: "#90d2a7"],
                [value: 15, color: "#44b621"],
                [value: 20, color: "#f1d801"],
                [value: 25, color: "#d04e00"],
                [value: 30, color: "#bc2323"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
                // Celsius color set (to switch, delete the 13 lines above anmd remove the two slashes at the beginning of the line below)
                //[value: 0, color: "#153591"], [value: 7, color: "#1e9cbb"], [value: 15, color: "#90d2a7"], [value: 23, color: "#44b621"], [value: 28, color: "#f1d801"], [value: 35, color: "#d04e00"], [value: 37, color: "#bc2323"]
            ]
        }
        
        valueTile("humidity", "device.humidity", width: 2, height: 2, unit: "%") {
            state("val", label:'${currentValue}%', defaultState: true, 
            	backgroundColors:[
                    [value: 10, color: "#153591"],
                    [value: 30, color: "#1e9cbb"],
                    [value: 40, color: "#90d2a7"],
                    [value: 50, color: "#44b621"],
                    [value: 60, color: "#f1d801"],
                    [value: 80, color: "#d04e00"],
                    [value: 90, color: "#bc2323"]
                ]
            )
        }
        
        
        valueTile("pressure", "device.pressure", width: 2, height: 2, unit: "") {
            state("val", label:'${currentValue} kpa', defaultState: true, 
            	backgroundColors:[
                    [value: 10, color: "#153591"],
                    [value: 30, color: "#1e9cbb"],
                    [value: 40, color: "#90d2a7"],
                    [value: 50, color: "#44b621"],
                    [value: 60, color: "#f1d801"],
                    [value: 80, color: "#d04e00"],
                    [value: 90, color: "#bc2323"]
                ]
            )
        }
        valueTile("battery", "device.battery", width: 2, height: 2) {
            state "val", label:'${currentValue}%', defaultState: true
        }		
        valueTile("pre", "device.pre", decoration: "flat", inactiveLabel: false, width: 2, height: 1) {
            state("val", label:'${currentValue}', defaultState: true)
        }
        valueTile("humi", "device.humi", decoration: "flat", inactiveLabel: false, width: 2, height: 1) {
            state("val", label:'${currentValue}', defaultState: true)
        }
        
        
        valueTile("1l", "device.1l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("1t", "device.1t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("1h", "device.1h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("2l", "device.2l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("2t", "device.2t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("2h", "device.2h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("3l", "device.3l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("3t", "device.3t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("3h", "device.3h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("4l", "device.4l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("4t", "device.4t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("4h", "device.4h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("5l", "device.5l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("5t", "device.5t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("5h", "device.5h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("6l", "device.6l") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("6t", "device.6t") {
            state "val", label:'${currentValue}', defaultState: true
        }		
        valueTile("6h", "device.6h") {
            state "val", label:'${currentValue}', defaultState: true
        }		
//        valueTile("lastcheckin", "device.lastCheckin", inactiveLabel: false, decoration:"flat", width: 4, height: 1) {
//        state "lastcheckin", label:'Last Event:\n ${currentValue}'
//        }
		valueTile("bat", "device.bat", decoration: "flat", inactiveLabel: false, width: 1, height: 1) {
            state("val", label:'${currentValue}', defaultState: true)
        }
        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
            state "default", label:"", action:"refresh", icon:"st.secondary.refresh"
        }
        htmlTile(name:"tempD",action:"tempD", type: "HTML",width: 6, height: 3, whitelist: whitelist())
        htmlTile(name:"tempW",action:"tempW", type: "HTML",width: 6, height: 3, whitelist: whitelist())
        htmlTile(name:"humiD",action:"humiD", type: "HTML",width: 6, height: 3, whitelist: whitelist())
        htmlTile(name:"humiW",action:"humiW", type: "HTML",width: 6, height: 3, whitelist: whitelist())

        main("temperature2")
        details(["temperature", "humi", "pre", "bat", "refresh", "humidity", "pressure", "battery",
            "1l", "2l", "3l", "4l", "5l", "6l", 
            "1t", "2t", "3t", "4t", "5t", "6t", 
            "1h", "2h", "3h", "4h", "5h", "6h", 
            "tempD", "humiD", "tempW", "humiW"
		])
    }
}
//-------------------------------------
mappings {
	path("/tempD") { action: [GET:"tempD"] }
	path("/humiD") { action: [GET:"humiD"] }
	path("/tempW") { action: [GET:"tempW"] }
	path("/humiW") { action: [GET:"humiW"] }
}
def tempD() {
	def html = """


<!DOCTYPE html>
<html style="height: 100%;">

<head>
<script type="text/javascript">window.NREUM||(NREUM={});NREUM.info={"beacon":"bam.nr-data.net","errorBeacon":"bam.nr-data.net","licenseKey":"199e8a4832","applicationID":"2972453","transactionName":"c11cQBFeWw5TSh9TXlFARkdMQl8NQQ==","queueTime":0,"applicationTime":6,"agent":""}</script>
<script type="text/javascript">window.NREUM||(NREUM={}),__nr_require=function(e,t,n){function r(n){if(!t[n]){var o=t[n]={exports:{}};e[n][0].call(o.exports,function(t){var o=e[n][1][t];return r(o||t)},o,o.exports)}return t[n].exports}if("function"==typeof __nr_require)return __nr_require;for(var o=0;o<n.length;o++)r(n[o]);return r}({1:[function(e,t,n){function r(){}function o(e,t,n){return function(){return i(e,[f.now()].concat(u(arguments)),t?null:this,n),t?void 0:this}}var i=e("handle"),a=e(2),u=e(3),c=e("ee").get("tracer"),f=e("loader"),s=NREUM;"undefined"==typeof window.newrelic&&(newrelic=s);var p=["setPageViewName","setCustomAttribute","setErrorHandler","finished","addToTrace","inlineHit","addRelease"],d="api-",l=d+"ixn-";a(p,function(e,t){s[t]=o(d+t,!0,"api")}),s.addPageAction=o(d+"addPageAction",!0),s.setCurrentRouteName=o(d+"routeName",!0),t.exports=newrelic,s.interaction=function(){return(new r).get()};var m=r.prototype={createTracer:function(e,t){var n={},r=this,o="function"==typeof t;return i(l+"tracer",[f.now(),e,n],r),function(){if(c.emit((o?"":"no-")+"fn-start",[f.now(),r,o],n),o)try{return t.apply(this,arguments)}catch(e){throw c.emit("fn-err",[arguments,this,e],n),e}finally{c.emit("fn-end",[f.now()],n)}}}};a("setName,setAttribute,save,ignore,onEnd,getContext,end,get".split(","),function(e,t){m[t]=o(l+t)}),newrelic.noticeError=function(e){"string"==typeof e&&(e=new Error(e)),i("err",[e,f.now()])}},{}],2:[function(e,t,n){function r(e,t){var n=[],r="",i=0;for(r in e)o.call(e,r)&&(n[i]=t(r,e[r]),i+=1);return n}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],3:[function(e,t,n){function r(e,t,n){t||(t=0),"undefined"==typeof n&&(n=e?e.length:0);for(var r=-1,o=n-t||0,i=Array(o<0?0:o);++r<o;)i[r]=e[t+r];return i}t.exports=r},{}],4:[function(e,t,n){t.exports={exists:"undefined"!=typeof window.performance&&window.performance.timing&&"undefined"!=typeof window.performance.timing.navigationStart}},{}],ee:[function(e,t,n){function r(){}function o(e){function t(e){return e&&e instanceof r?e:e?c(e,u,i):i()}function n(n,r,o,i){if(!d.aborted||i){e&&e(n,r,o);for(var a=t(o),u=m(n),c=u.length,f=0;f<c;f++)u[f].apply(a,r);var p=s[y[n]];return p&&p.push([b,n,r,a]),a}}function l(e,t){v[e]=m(e).concat(t)}function m(e){return v[e]||[]}function w(e){return p[e]=p[e]||o(n)}function g(e,t){f(e,function(e,n){t=t||"feature",y[n]=t,t in s||(s[t]=[])})}var v={},y={},b={on:l,emit:n,get:w,listeners:m,context:t,buffer:g,abort:a,aborted:!1};return b}function i(){return new r}function a(){(s.api||s.feature)&&(d.aborted=!0,s=d.backlog={})}var u="nr@context",c=e("gos"),f=e(2),s={},p={},d=t.exports=o();d.backlog=s},{}],gos:[function(e,t,n){function r(e,t,n){if(o.call(e,t))return e[t];var r=n();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(e,t,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return e[t]=r,r}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],handle:[function(e,t,n){function r(e,t,n,r){o.buffer([e],r),o.emit(e,t,n)}var o=e("ee").get("handle");t.exports=r,r.ee=o},{}],id:[function(e,t,n){function r(e){var t=typeof e;return!e||"object"!==t&&"function"!==t?-1:e===window?0:a(e,i,function(){return o++})}var o=1,i="nr@id",a=e("gos");t.exports=r},{}],loader:[function(e,t,n){function r(){if(!x++){var e=h.info=NREUM.info,t=d.getElementsByTagName("script")[0];if(setTimeout(s.abort,3e4),!(e&&e.licenseKey&&e.applicationID&&t))return s.abort();f(y,function(t,n){e[t]||(e[t]=n)}),c("mark",["onload",a()+h.offset],null,"api");var n=d.createElement("script");n.src="https://"+e.agent,t.parentNode.insertBefore(n,t)}}function o(){"complete"===d.readyState&&i()}function i(){c("mark",["domContent",a()+h.offset],null,"api")}function a(){return E.exists&&performance.now?Math.round(performance.now()):(u=Math.max((new Date).getTime(),u))-h.offset}var u=(new Date).getTime(),c=e("handle"),f=e(2),s=e("ee"),p=window,d=p.document,l="addEventListener",m="attachEvent",w=p.XMLHttpRequest,g=w&&w.prototype;NREUM.o={ST:setTimeout,SI:p.setImmediate,CT:clearTimeout,XHR:w,REQ:p.Request,EV:p.Event,PR:p.Promise,MO:p.MutationObserver};var v=""+location,y={beacon:"bam.nr-data.net",errorBeacon:"bam.nr-data.net",agent:"js-agent.newrelic.com/nr-1071.min.js"},b=w&&g&&g[l]&&!/CriOS/.test(navigator.userAgent),h=t.exports={offset:u,now:a,origin:v,features:{},xhrWrappable:b};e(1),d[l]?(d[l]("DOMContentLoaded",i,!1),p[l]("load",r,!1)):(d[m]("onreadystatechange",o),p[m]("onload",r)),c("mark",["firstbyte",u],null,"api");var x=0,E=e(4)},{}]},{},["loader"]);</script>
  <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="//thingspeak.com/highcharts-3.0.8.js"></script>
  <script src="/assets/application-03e2c06ff0f3b0568246067907657e6d.js"></script>
    <script type="text/javascript">
    // user's timezone offset
    var myOffset = new Date().getTimezoneOffset();

    // converts date format from JSON
    function getChartDate(d) {
      // offset in minutes is converted to milliseconds and subtracted so that chart's x-axis is correct
      return Date.parse(d) - (myOffset * 60000);
    }

    \$(document).on('page:load ready', function() {
      // blank array for holding chart data
      var chartData = [];
      // variable for the local date in milliseconds
      var localDate;
      // variable for the last date added to the chart
      var last_date;

      // get the data with a webservice call
      \$.getJSON('https://thingspeak.com/channels/${channel}/field/${tempField}.json?callback=?&amp;offset=0&amp;days=1&amp;timescale=30', function(data) {

          // if no access
          if (data == '-1') {
            \$('#chart-container').append('This channel is not public.  To embed charts, the channel must be public or a read key must be specified.');
          }

          // iterate through each feed
          \$.each(data.feeds, function() {
            var p = new Highcharts.Point();
            // set the proper values
            var v = this.field${tempField};
            p.x = getChartDate(this.created_at);
            p.y = parseFloat(v);
            // add location if possible
            if (this.location) {
              p.name = this.location;
            }
            // if a numerical value exists add it
            if (!isNaN(parseInt(v))
                            ) {
              chartData.push(p);
            }
          });

          // specify the chart options
          var chartOptions = {
            chart: {
              renderTo: 'chart-container',
              defaultSeriesType: 'line',
              backgroundColor: '#ffffff',
              style: {
                fontSize: '15pt',
                fontWeight: 'bold'
              },              
              events: {
                load: function() {
                  //if dynamic and no "timeslice" options are set
                  //   GAK 02/16/2013 Let's try to add the last "average" slice if params[:average]

                  var url = 'https://thingspeak.com/channels/${channel}/feed/last.json?callback=?&amp;offset=0&amp;location=true&amp;days=1&amp;timescale=30';
                  if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_average.json?callback=?&amp;offset=0&amp;location=true&amp;average=&amp;days=1&amp;timescale=30';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_median.json?callback=?&amp;offset=0&amp;location=true&amp;median=&amp;days=1&amp;timescale=30';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_sum.json?callback=?&amp;offset=0&amp;location=true&amp;sum=&amp;days=1&amp;timescale=30';
                  }

                  if ('true' === 'true' && ('30'.length < 1)) {
                    // push data every 15 seconds
                    setInterval(function() {
                      // get the data with a webservice call if we're just getting the last channel
                      \$.getJSON(url, function(data) {
                        // if data exists
                        if (data && data.field${tempField}) {

                          var p = new Highcharts.Point();
                          // set the proper values
                          var v = data.field${tempField};

                          p.x = getChartDate(data.created_at);
                          p.y = parseFloat(v);
                          // add location if possible
                          if (data.location) {
                            p.name = data.location;
                          }
                          // get the last date if possible
                          if (dynamicChart.series[0].data.length > 0) {
                            last_date = dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].x;
                          }
                          var shift = false; //default for shift

                          //if push is requested in parameters
                          // then if results is and data.length is < results, shift = false
                          var results = 360;

                          if (results && dynamicChart.series[0].data.length + 1 >= results) {
                            shift = true;
                          }
                          // if a numerical value exists and it is a new date, add it
                          if (!isNaN(parseInt(v)) && (p.x != last_date)
                                                        ) {
                            dynamicChart.series[0].addPoint(p, true, shift);
                          } else {
                            dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].update(p);
                          }
                        }
                      });

                    }, 15000);
                  }
                }
              }
            },
            title: {
              text: '',
              style: {
                fontSize: '20pt',
                color: '#d60202',
                fontWeight: 'bold'
              }
            },
            plotOptions: {
              line: {
                color: '#d62020'
              },
              bar: {
                color: '#d62020'
              },
              column: {
                color: '#d62020'
              },
              spline: {
                color: '#d62020'
              },
              series: {
                marker: {
                  radius: 3
                },
                animation: true,
                step: false,
                borderWidth: 0,
                turboThreshold: 0
              }
            },
            exporting: {
              enabled: false
            },
            tooltip: {
              // reformat the tooltips so that local times are displayed
              formatter: function() {
                var d = new Date(this.x + (myOffset * 60000));
                var n = (this.point.name === undefined) ? '' : '<br/>' + this.point.name;
                return this.series.name + ':<b>' + this.y + '</b>' + n + '<br/>' + d.toDateString() + '<br/>' + d.toTimeString().replace();
              }
            },
            xAxis: {
              type: 'datetime',
              title: {
                text: 'test',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              }
            },
            yAxis: {
              title: {
                text: '',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              },
              min: null ,
              max: null
            },
            legend: {
              enabled: false
            },
            series: [{
              name: data.channel.field${tempField}
            }],
            credits: {
              text: 'ThingSpeak.com',
              href: 'https://thingspeak.com/',
              style: {
                color: '#D62020'
              }
            }
          };

          // add the data to the chart
          chartOptions.series[0].data = chartData;

          // set chart labels here so that decoding occurs properly
          chartOptions.title.text = decodeURIComponent('Temperature%20-%2024h');
          chartOptions.xAxis.title.text = decodeURIComponent('time');
          chartOptions.yAxis.title.text = decodeURIComponent('%C2%B0C');

          // draw the chart
          var dynamicChart = new Highcharts.Chart(chartOptions);

          // end getJSON success
        })
        // chained to getjson, on error
        .error(function() {
          \$('#chart-container').html('Invalid Channel.');
        });

    }); // end document.ready
    </script>
</head>

<body style='background-color: #ffffff; height: 100%; margin: 0; padding: 0;'>
  <div id="chart-container" style="width: 945px; height: 450px; display: block; position:absolute; bottom:0; top:0; left:0; right:0; margin: 5px 15px 15px 0; overflow: hidden;">
    <img alt="Loader transparent" src="/assets/loader-transparent-dcfdb186859b297e13cf2c077ca08e36.gif" style="position: absolute; margin: auto; top: 0; left: 0; right: 0; bottom: 0;" />
  </div>
</body>

</html>



"""
 render contentType: "text/html", data: html, status: 200
}


def humiD() {
	def html = """


<!DOCTYPE html>
<html style="height: 100%;">

<head>
<script type="text/javascript">window.NREUM||(NREUM={});NREUM.info={"beacon":"bam.nr-data.net","errorBeacon":"bam.nr-data.net","licenseKey":"199e8a4832","applicationID":"2972453","transactionName":"c11cQBFeWw5TSh9TXlFARkdMQl8NQQ==","queueTime":0,"applicationTime":6,"agent":""}</script>
<script type="text/javascript">window.NREUM||(NREUM={}),__nr_require=function(e,t,n){function r(n){if(!t[n]){var o=t[n]={exports:{}};e[n][0].call(o.exports,function(t){var o=e[n][1][t];return r(o||t)},o,o.exports)}return t[n].exports}if("function"==typeof __nr_require)return __nr_require;for(var o=0;o<n.length;o++)r(n[o]);return r}({1:[function(e,t,n){function r(){}function o(e,t,n){return function(){return i(e,[f.now()].concat(u(arguments)),t?null:this,n),t?void 0:this}}var i=e("handle"),a=e(2),u=e(3),c=e("ee").get("tracer"),f=e("loader"),s=NREUM;"undefined"==typeof window.newrelic&&(newrelic=s);var p=["setPageViewName","setCustomAttribute","setErrorHandler","finished","addToTrace","inlineHit","addRelease"],d="api-",l=d+"ixn-";a(p,function(e,t){s[t]=o(d+t,!0,"api")}),s.addPageAction=o(d+"addPageAction",!0),s.setCurrentRouteName=o(d+"routeName",!0),t.exports=newrelic,s.interaction=function(){return(new r).get()};var m=r.prototype={createTracer:function(e,t){var n={},r=this,o="function"==typeof t;return i(l+"tracer",[f.now(),e,n],r),function(){if(c.emit((o?"":"no-")+"fn-start",[f.now(),r,o],n),o)try{return t.apply(this,arguments)}catch(e){throw c.emit("fn-err",[arguments,this,e],n),e}finally{c.emit("fn-end",[f.now()],n)}}}};a("setName,setAttribute,save,ignore,onEnd,getContext,end,get".split(","),function(e,t){m[t]=o(l+t)}),newrelic.noticeError=function(e){"string"==typeof e&&(e=new Error(e)),i("err",[e,f.now()])}},{}],2:[function(e,t,n){function r(e,t){var n=[],r="",i=0;for(r in e)o.call(e,r)&&(n[i]=t(r,e[r]),i+=1);return n}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],3:[function(e,t,n){function r(e,t,n){t||(t=0),"undefined"==typeof n&&(n=e?e.length:0);for(var r=-1,o=n-t||0,i=Array(o<0?0:o);++r<o;)i[r]=e[t+r];return i}t.exports=r},{}],4:[function(e,t,n){t.exports={exists:"undefined"!=typeof window.performance&&window.performance.timing&&"undefined"!=typeof window.performance.timing.navigationStart}},{}],ee:[function(e,t,n){function r(){}function o(e){function t(e){return e&&e instanceof r?e:e?c(e,u,i):i()}function n(n,r,o,i){if(!d.aborted||i){e&&e(n,r,o);for(var a=t(o),u=m(n),c=u.length,f=0;f<c;f++)u[f].apply(a,r);var p=s[y[n]];return p&&p.push([b,n,r,a]),a}}function l(e,t){v[e]=m(e).concat(t)}function m(e){return v[e]||[]}function w(e){return p[e]=p[e]||o(n)}function g(e,t){f(e,function(e,n){t=t||"feature",y[n]=t,t in s||(s[t]=[])})}var v={},y={},b={on:l,emit:n,get:w,listeners:m,context:t,buffer:g,abort:a,aborted:!1};return b}function i(){return new r}function a(){(s.api||s.feature)&&(d.aborted=!0,s=d.backlog={})}var u="nr@context",c=e("gos"),f=e(2),s={},p={},d=t.exports=o();d.backlog=s},{}],gos:[function(e,t,n){function r(e,t,n){if(o.call(e,t))return e[t];var r=n();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(e,t,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return e[t]=r,r}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],handle:[function(e,t,n){function r(e,t,n,r){o.buffer([e],r),o.emit(e,t,n)}var o=e("ee").get("handle");t.exports=r,r.ee=o},{}],id:[function(e,t,n){function r(e){var t=typeof e;return!e||"object"!==t&&"function"!==t?-1:e===window?0:a(e,i,function(){return o++})}var o=1,i="nr@id",a=e("gos");t.exports=r},{}],loader:[function(e,t,n){function r(){if(!x++){var e=h.info=NREUM.info,t=d.getElementsByTagName("script")[0];if(setTimeout(s.abort,3e4),!(e&&e.licenseKey&&e.applicationID&&t))return s.abort();f(y,function(t,n){e[t]||(e[t]=n)}),c("mark",["onload",a()+h.offset],null,"api");var n=d.createElement("script");n.src="https://"+e.agent,t.parentNode.insertBefore(n,t)}}function o(){"complete"===d.readyState&&i()}function i(){c("mark",["domContent",a()+h.offset],null,"api")}function a(){return E.exists&&performance.now?Math.round(performance.now()):(u=Math.max((new Date).getTime(),u))-h.offset}var u=(new Date).getTime(),c=e("handle"),f=e(2),s=e("ee"),p=window,d=p.document,l="addEventListener",m="attachEvent",w=p.XMLHttpRequest,g=w&&w.prototype;NREUM.o={ST:setTimeout,SI:p.setImmediate,CT:clearTimeout,XHR:w,REQ:p.Request,EV:p.Event,PR:p.Promise,MO:p.MutationObserver};var v=""+location,y={beacon:"bam.nr-data.net",errorBeacon:"bam.nr-data.net",agent:"js-agent.newrelic.com/nr-1071.min.js"},b=w&&g&&g[l]&&!/CriOS/.test(navigator.userAgent),h=t.exports={offset:u,now:a,origin:v,features:{},xhrWrappable:b};e(1),d[l]?(d[l]("DOMContentLoaded",i,!1),p[l]("load",r,!1)):(d[m]("onreadystatechange",o),p[m]("onload",r)),c("mark",["firstbyte",u],null,"api");var x=0,E=e(4)},{}]},{},["loader"]);</script>
  <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="//thingspeak.com/highcharts-3.0.8.js"></script>
  <script src="/assets/application-03e2c06ff0f3b0568246067907657e6d.js"></script>
    <script type="text/javascript">
    // user's timezone offset
    var myOffset = new Date().getTimezoneOffset();

    // converts date format from JSON
    function getChartDate(d) {
      // offset in minutes is converted to milliseconds and subtracted so that chart's x-axis is correct
      return Date.parse(d) - (myOffset * 60000);
    }

    \$(document).on('page:load ready', function() {
      // blank array for holding chart data
      var chartData = [];
      // variable for the local date in milliseconds
      var localDate;
      // variable for the last date added to the chart
      var last_date;

      // get the data with a webservice call
      \$.getJSON('https://thingspeak.com/channels/${channel}/field/${humiField}.json?callback=?&amp;offset=0&amp;days=1&amp;timescale=30', function(data) {

          // if no access
          if (data == '-1') {
            \$('#chart-container').append('This channel is not public.  To embed charts, the channel must be public or a read key must be specified.');
          }

          // iterate through each feed
          \$.each(data.feeds, function() {
            var p = new Highcharts.Point();
            // set the proper values
            var v = this.field${humiField};
            p.x = getChartDate(this.created_at);
            p.y = parseFloat(v);
            // add location if possible
            if (this.location) {
              p.name = this.location;
            }
            // if a numerical value exists add it
            if (!isNaN(parseInt(v))
                            ) {
              chartData.push(p);
            }
          });

          // specify the chart options
          var chartOptions = {
            chart: {
              renderTo: 'chart-container',
              defaultSeriesType: 'line',
              backgroundColor: '#ffffff',
              style: {
                fontSize: '15pt',
                fontWeight: 'bold'
              },              
              events: {
                load: function() {
                  //if dynamic and no "timeslice" options are set
                  //   GAK 02/16/2013 Let's try to add the last "average" slice if params[:average]

                  var url = 'https://thingspeak.com/channels/${channel}/feed/last.json?callback=?&amp;offset=0&amp;location=true&amp;days=1&amp;timescale=30';
                  if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_average.json?callback=?&amp;offset=0&amp;location=true&amp;average=&amp;days=1&amp;timescale=30';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_median.json?callback=?&amp;offset=0&amp;location=true&amp;median=&amp;days=1&amp;timescale=30';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_sum.json?callback=?&amp;offset=0&amp;location=true&amp;sum=&amp;days=1&amp;timescale=30';
                  }

                  if ('true' === 'true' && ('30'.length < 1)) {
                    // push data every 15 seconds
                    setInterval(function() {
                      // get the data with a webservice call if we're just getting the last channel
                      \$.getJSON(url, function(data) {
                        // if data exists
                        if (data && data.field${humiField}) {

                          var p = new Highcharts.Point();
                          // set the proper values
                          var v = data.field${humiField};

                          p.x = getChartDate(data.created_at);
                          p.y = parseFloat(v);
                          // add location if possible
                          if (data.location) {
                            p.name = data.location;
                          }
                          // get the last date if possible
                          if (dynamicChart.series[0].data.length > 0) {
                            last_date = dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].x;
                          }
                          var shift = false; //default for shift

                          //if push is requested in parameters
                          // then if results is and data.length is < results, shift = false
                          var results = 360;

                          if (results && dynamicChart.series[0].data.length + 1 >= results) {
                            shift = true;
                          }
                          // if a numerical value exists and it is a new date, add it
                          if (!isNaN(parseInt(v)) && (p.x != last_date)
                                                        ) {
                            dynamicChart.series[0].addPoint(p, true, shift);
                          } else {
                            dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].update(p);
                          }
                        }
                      });

                    }, 15000);
                  }
                }
              }
            },
            title: {
              text: '',
              style: {
                fontSize: '20pt',
                color: '#0003ce',
                fontWeight: 'bold'
              }
            },
            plotOptions: {
              line: {
                color: '#018ec1'
              },
              bar: {
                color: '#018ec1'
              },
              column: {
                color: '#018ec1'
              },
              spline: {
                color: '#018ec1'
              },
              series: {
                marker: {
                  radius: 3
                },
                animation: true,
                step: false,
                borderWidth: 0,
                turboThreshold: 0
              }
            },
            exporting: {
              enabled: false
            },
            tooltip: {
              // reformat the tooltips so that local times are displayed
              formatter: function() {
                var d = new Date(this.x + (myOffset * 60000));
                var n = (this.point.name === undefined) ? '' : '<br/>' + this.point.name;
                return this.series.name + ':<b>' + this.y + '</b>' + n + '<br/>' + d.toDateString() + '<br/>' + d.toTimeString().replace();
              }
            },
            xAxis: {
              type: 'datetime',
              title: {
                text: 'test',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              }
            },
            yAxis: {
              title: {
                text: '',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              },
              min: null ,
              max: null
            },
            legend: {
              enabled: false
            },
            series: [{
              name: data.channel.field${humiField}
            }],
            credits: {
              text: 'ThingSpeak.com',
              href: 'https://thingspeak.com/',
              style: {
                color: '#D62020'
              }
            }
          };

          // add the data to the chart
          chartOptions.series[0].data = chartData;

          // set chart labels here so that decoding occurs properly
          chartOptions.title.text = decodeURIComponent('Humidity%20-%2024h');
          chartOptions.xAxis.title.text = decodeURIComponent('time');
          chartOptions.yAxis.title.text = decodeURIComponent('%25');

          // draw the chart
          var dynamicChart = new Highcharts.Chart(chartOptions);

          // end getJSON success
        })
        // chained to getjson, on error
        .error(function() {
          \$('#chart-container').html('Invalid Channel.');
        });

    }); // end document.ready
    </script>
</head>

<body style='background-color: #ffffff; height: 100%; margin: 0; padding: 0;'>
  <div id="chart-container" style="width: 945px; height: 450px; display: block; position:absolute; bottom:0; top:0; left:0; right:0; margin: 5px 15px 15px 0; overflow: hidden;">
    <img alt="Loader transparent" src="/assets/loader-transparent-dcfdb186859b297e13cf2c077ca08e36.gif" style="position: absolute; margin: auto; top: 0; left: 0; right: 0; bottom: 0;" />
  </div>
</body>

</html>


"""
 render contentType: "text/html", data: html, status: 200
}

def tempW() {
	def html = """

<!DOCTYPE html>
<html style="height: 100%;">

<head>
<script type="text/javascript">window.NREUM||(NREUM={});NREUM.info={"beacon":"bam.nr-data.net","errorBeacon":"bam.nr-data.net","licenseKey":"199e8a4832","applicationID":"2972453","transactionName":"c11cQBFeWw5TSh9TXlFARkdMQl8NQQ==","queueTime":0,"applicationTime":6,"agent":""}</script>
<script type="text/javascript">window.NREUM||(NREUM={}),__nr_require=function(e,t,n){function r(n){if(!t[n]){var o=t[n]={exports:{}};e[n][0].call(o.exports,function(t){var o=e[n][1][t];return r(o||t)},o,o.exports)}return t[n].exports}if("function"==typeof __nr_require)return __nr_require;for(var o=0;o<n.length;o++)r(n[o]);return r}({1:[function(e,t,n){function r(){}function o(e,t,n){return function(){return i(e,[f.now()].concat(u(arguments)),t?null:this,n),t?void 0:this}}var i=e("handle"),a=e(2),u=e(3),c=e("ee").get("tracer"),f=e("loader"),s=NREUM;"undefined"==typeof window.newrelic&&(newrelic=s);var p=["setPageViewName","setCustomAttribute","setErrorHandler","finished","addToTrace","inlineHit","addRelease"],d="api-",l=d+"ixn-";a(p,function(e,t){s[t]=o(d+t,!0,"api")}),s.addPageAction=o(d+"addPageAction",!0),s.setCurrentRouteName=o(d+"routeName",!0),t.exports=newrelic,s.interaction=function(){return(new r).get()};var m=r.prototype={createTracer:function(e,t){var n={},r=this,o="function"==typeof t;return i(l+"tracer",[f.now(),e,n],r),function(){if(c.emit((o?"":"no-")+"fn-start",[f.now(),r,o],n),o)try{return t.apply(this,arguments)}catch(e){throw c.emit("fn-err",[arguments,this,e],n),e}finally{c.emit("fn-end",[f.now()],n)}}}};a("setName,setAttribute,save,ignore,onEnd,getContext,end,get".split(","),function(e,t){m[t]=o(l+t)}),newrelic.noticeError=function(e){"string"==typeof e&&(e=new Error(e)),i("err",[e,f.now()])}},{}],2:[function(e,t,n){function r(e,t){var n=[],r="",i=0;for(r in e)o.call(e,r)&&(n[i]=t(r,e[r]),i+=1);return n}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],3:[function(e,t,n){function r(e,t,n){t||(t=0),"undefined"==typeof n&&(n=e?e.length:0);for(var r=-1,o=n-t||0,i=Array(o<0?0:o);++r<o;)i[r]=e[t+r];return i}t.exports=r},{}],4:[function(e,t,n){t.exports={exists:"undefined"!=typeof window.performance&&window.performance.timing&&"undefined"!=typeof window.performance.timing.navigationStart}},{}],ee:[function(e,t,n){function r(){}function o(e){function t(e){return e&&e instanceof r?e:e?c(e,u,i):i()}function n(n,r,o,i){if(!d.aborted||i){e&&e(n,r,o);for(var a=t(o),u=m(n),c=u.length,f=0;f<c;f++)u[f].apply(a,r);var p=s[y[n]];return p&&p.push([b,n,r,a]),a}}function l(e,t){v[e]=m(e).concat(t)}function m(e){return v[e]||[]}function w(e){return p[e]=p[e]||o(n)}function g(e,t){f(e,function(e,n){t=t||"feature",y[n]=t,t in s||(s[t]=[])})}var v={},y={},b={on:l,emit:n,get:w,listeners:m,context:t,buffer:g,abort:a,aborted:!1};return b}function i(){return new r}function a(){(s.api||s.feature)&&(d.aborted=!0,s=d.backlog={})}var u="nr@context",c=e("gos"),f=e(2),s={},p={},d=t.exports=o();d.backlog=s},{}],gos:[function(e,t,n){function r(e,t,n){if(o.call(e,t))return e[t];var r=n();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(e,t,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return e[t]=r,r}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],handle:[function(e,t,n){function r(e,t,n,r){o.buffer([e],r),o.emit(e,t,n)}var o=e("ee").get("handle");t.exports=r,r.ee=o},{}],id:[function(e,t,n){function r(e){var t=typeof e;return!e||"object"!==t&&"function"!==t?-1:e===window?0:a(e,i,function(){return o++})}var o=1,i="nr@id",a=e("gos");t.exports=r},{}],loader:[function(e,t,n){function r(){if(!x++){var e=h.info=NREUM.info,t=d.getElementsByTagName("script")[0];if(setTimeout(s.abort,3e4),!(e&&e.licenseKey&&e.applicationID&&t))return s.abort();f(y,function(t,n){e[t]||(e[t]=n)}),c("mark",["onload",a()+h.offset],null,"api");var n=d.createElement("script");n.src="https://"+e.agent,t.parentNode.insertBefore(n,t)}}function o(){"complete"===d.readyState&&i()}function i(){c("mark",["domContent",a()+h.offset],null,"api")}function a(){return E.exists&&performance.now?Math.round(performance.now()):(u=Math.max((new Date).getTime(),u))-h.offset}var u=(new Date).getTime(),c=e("handle"),f=e(2),s=e("ee"),p=window,d=p.document,l="addEventListener",m="attachEvent",w=p.XMLHttpRequest,g=w&&w.prototype;NREUM.o={ST:setTimeout,SI:p.setImmediate,CT:clearTimeout,XHR:w,REQ:p.Request,EV:p.Event,PR:p.Promise,MO:p.MutationObserver};var v=""+location,y={beacon:"bam.nr-data.net",errorBeacon:"bam.nr-data.net",agent:"js-agent.newrelic.com/nr-1071.min.js"},b=w&&g&&g[l]&&!/CriOS/.test(navigator.userAgent),h=t.exports={offset:u,now:a,origin:v,features:{},xhrWrappable:b};e(1),d[l]?(d[l]("DOMContentLoaded",i,!1),p[l]("load",r,!1)):(d[m]("onreadystatechange",o),p[m]("onload",r)),c("mark",["firstbyte",u],null,"api");var x=0,E=e(4)},{}]},{},["loader"]);</script>
  <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="//thingspeak.com/highcharts-3.0.8.js"></script>
  <script src="/assets/application-03e2c06ff0f3b0568246067907657e6d.js"></script>
    <script type="text/javascript">
    // user's timezone offset
    var myOffset = new Date().getTimezoneOffset();

    // converts date format from JSON
    function getChartDate(d) {
      // offset in minutes is converted to milliseconds and subtracted so that chart's x-axis is correct
      return Date.parse(d) - (myOffset * 60000);
    }

    \$(document).on('page:load ready', function() {
      // blank array for holding chart data
      var chartData = [];
      // variable for the local date in milliseconds
      var localDate;
      // variable for the last date added to the chart
      var last_date;

      // get the data with a webservice call
      \$.getJSON('https://thingspeak.com/channels/${channel}/field/${tempField}.json?callback=?&amp;offset=0&amp;days=7&amp;timescale=240', function(data) {

          // if no access
          if (data == '-1') {
            \$('#chart-container').append('This channel is not public.  To embed charts, the channel must be public or a read key must be specified.');
          }

          // iterate through each feed
          \$.each(data.feeds, function() {
            var p = new Highcharts.Point();
            // set the proper values
            var v = this.field${tempField};
            p.x = getChartDate(this.created_at);
            p.y = parseFloat(v);
            // add location if possible
            if (this.location) {
              p.name = this.location;
            }
            // if a numerical value exists add it
            if (!isNaN(parseInt(v))
                            ) {
              chartData.push(p);
            }
          });

          // specify the chart options
          var chartOptions = {
            chart: {
              renderTo: 'chart-container',
              defaultSeriesType: 'line',
              backgroundColor: '#ffffff',
              style: {
                fontSize: '15pt',
                fontWeight: 'bold'
              },              
              events: {
                load: function() {
                  //if dynamic and no "timeslice" options are set
                  //   GAK 02/16/2013 Let's try to add the last "average" slice if params[:average]

                  var url = 'https://thingspeak.com/channels/${channel}/feed/last.json?callback=?&amp;offset=0&amp;location=true&amp;days=7&amp;timescale=240';
                  if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_average.json?callback=?&amp;offset=0&amp;location=true&amp;average=&amp;days=7&amp;timescale=240';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_median.json?callback=?&amp;offset=0&amp;location=true&amp;median=&amp;days=7&amp;timescale=240';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_sum.json?callback=?&amp;offset=0&amp;location=true&amp;sum=&amp;days=7&amp;timescale=240';
                  }

                  if ('true' === 'true' && ('30'.length < 1)) {
                    // push data every 15 seconds
                    setInterval(function() {
                      // get the data with a webservice call if we're just getting the last channel
                      \$.getJSON(url, function(data) {
                        // if data exists
                        if (data && data.field${tempField}) {

                          var p = new Highcharts.Point();
                          // set the proper values
                          var v = data.field${tempField};

                          p.x = getChartDate(data.created_at);
                          p.y = parseFloat(v);
                          // add location if possible
                          if (data.location) {
                            p.name = data.location;
                          }
                          // get the last date if possible
                          if (dynamicChart.series[0].data.length > 0) {
                            last_date = dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].x;
                          }
                          var shift = false; //default for shift

                          //if push is requested in parameters
                          // then if results is and data.length is < results, shift = false
                          var results = 360;

                          if (results && dynamicChart.series[0].data.length + 1 >= results) {
                            shift = true;
                          }
                          // if a numerical value exists and it is a new date, add it
                          if (!isNaN(parseInt(v)) && (p.x != last_date)
                                                        ) {
                            dynamicChart.series[0].addPoint(p, true, shift);
                          } else {
                            dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].update(p);
                          }
                        }
                      });

                    }, 15000);
                  }
                }
              }
            },
            title: {
              text: '',
              style: {
                fontSize: '20pt',
                color: '#d60202',
                fontWeight: 'bold'
              }
            },
            plotOptions: {
              line: {
                color: '#d62020'
              },
              bar: {
                color: '#d62020'
              },
              column: {
                color: '#d62020'
              },
              spline: {
                color: '#d62020'
              },
              series: {
                marker: {
                  radius: 3
                },
                animation: true,
                step: false,
                borderWidth: 0,
                turboThreshold: 0
              }
            },
            exporting: {
              enabled: false
            },
            tooltip: {
              // reformat the tooltips so that local times are displayed
              formatter: function() {
                var d = new Date(this.x + (myOffset * 60000));
                var n = (this.point.name === undefined) ? '' : '<br/>' + this.point.name;
                return this.series.name + ':<b>' + this.y + '</b>' + n + '<br/>' + d.toDateString() + '<br/>' + d.toTimeString().replace();
              }
            },
            xAxis: {
              type: 'datetime',
              title: {
                text: 'test',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              }
            },
            yAxis: {
              title: {
                text: '',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              },
              min: null ,
              max: null
            },
            legend: {
              enabled: false
            },
            series: [{
              name: data.channel.field${tempField}
            }],
            credits: {
              text: 'ThingSpeak.com',
              href: 'https://thingspeak.com/',
              style: {
                color: '#D62020'
              }
            }
          };

          // add the data to the chart
          chartOptions.series[0].data = chartData;

          // set chart labels here so that decoding occurs properly
          chartOptions.title.text = decodeURIComponent('Temperature%20-%20Week');
          chartOptions.xAxis.title.text = decodeURIComponent('time');
          chartOptions.yAxis.title.text = decodeURIComponent('%C2%B0C');

          // draw the chart
          var dynamicChart = new Highcharts.Chart(chartOptions);

          // end getJSON success
        })
        // chained to getjson, on error
        .error(function() {
          \$('#chart-container').html('Invalid Channel.');
        });

    }); // end document.ready
    </script>
</head>

<body style='background-color: #ffffff; height: 100%; margin: 0; padding: 0;'>
  <div id="chart-container" style="width: 945px; height: 450px; display: block; position:absolute; bottom:0; top:0; left:0; right:0; margin: 5px 15px 15px 0; overflow: hidden;">
    <img alt="Loader transparent" src="/assets/loader-transparent-dcfdb186859b297e13cf2c077ca08e36.gif" style="position: absolute; margin: auto; top: 0; left: 0; right: 0; bottom: 0;" />
  </div>
</body>

</html>


"""
 render contentType: "text/html", data: html, status: 200
}


def humiW() {
	def html = """

<!DOCTYPE html>
<html style="height: 100%;">

<head>
<script type="text/javascript">window.NREUM||(NREUM={});NREUM.info={"beacon":"bam.nr-data.net","errorBeacon":"bam.nr-data.net","licenseKey":"199e8a4832","applicationID":"2972453","transactionName":"c11cQBFeWw5TSh9TXlFARkdMQl8NQQ==","queueTime":0,"applicationTime":6,"agent":""}</script>
<script type="text/javascript">window.NREUM||(NREUM={}),__nr_require=function(e,t,n){function r(n){if(!t[n]){var o=t[n]={exports:{}};e[n][0].call(o.exports,function(t){var o=e[n][1][t];return r(o||t)},o,o.exports)}return t[n].exports}if("function"==typeof __nr_require)return __nr_require;for(var o=0;o<n.length;o++)r(n[o]);return r}({1:[function(e,t,n){function r(){}function o(e,t,n){return function(){return i(e,[f.now()].concat(u(arguments)),t?null:this,n),t?void 0:this}}var i=e("handle"),a=e(2),u=e(3),c=e("ee").get("tracer"),f=e("loader"),s=NREUM;"undefined"==typeof window.newrelic&&(newrelic=s);var p=["setPageViewName","setCustomAttribute","setErrorHandler","finished","addToTrace","inlineHit","addRelease"],d="api-",l=d+"ixn-";a(p,function(e,t){s[t]=o(d+t,!0,"api")}),s.addPageAction=o(d+"addPageAction",!0),s.setCurrentRouteName=o(d+"routeName",!0),t.exports=newrelic,s.interaction=function(){return(new r).get()};var m=r.prototype={createTracer:function(e,t){var n={},r=this,o="function"==typeof t;return i(l+"tracer",[f.now(),e,n],r),function(){if(c.emit((o?"":"no-")+"fn-start",[f.now(),r,o],n),o)try{return t.apply(this,arguments)}catch(e){throw c.emit("fn-err",[arguments,this,e],n),e}finally{c.emit("fn-end",[f.now()],n)}}}};a("setName,setAttribute,save,ignore,onEnd,getContext,end,get".split(","),function(e,t){m[t]=o(l+t)}),newrelic.noticeError=function(e){"string"==typeof e&&(e=new Error(e)),i("err",[e,f.now()])}},{}],2:[function(e,t,n){function r(e,t){var n=[],r="",i=0;for(r in e)o.call(e,r)&&(n[i]=t(r,e[r]),i+=1);return n}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],3:[function(e,t,n){function r(e,t,n){t||(t=0),"undefined"==typeof n&&(n=e?e.length:0);for(var r=-1,o=n-t||0,i=Array(o<0?0:o);++r<o;)i[r]=e[t+r];return i}t.exports=r},{}],4:[function(e,t,n){t.exports={exists:"undefined"!=typeof window.performance&&window.performance.timing&&"undefined"!=typeof window.performance.timing.navigationStart}},{}],ee:[function(e,t,n){function r(){}function o(e){function t(e){return e&&e instanceof r?e:e?c(e,u,i):i()}function n(n,r,o,i){if(!d.aborted||i){e&&e(n,r,o);for(var a=t(o),u=m(n),c=u.length,f=0;f<c;f++)u[f].apply(a,r);var p=s[y[n]];return p&&p.push([b,n,r,a]),a}}function l(e,t){v[e]=m(e).concat(t)}function m(e){return v[e]||[]}function w(e){return p[e]=p[e]||o(n)}function g(e,t){f(e,function(e,n){t=t||"feature",y[n]=t,t in s||(s[t]=[])})}var v={},y={},b={on:l,emit:n,get:w,listeners:m,context:t,buffer:g,abort:a,aborted:!1};return b}function i(){return new r}function a(){(s.api||s.feature)&&(d.aborted=!0,s=d.backlog={})}var u="nr@context",c=e("gos"),f=e(2),s={},p={},d=t.exports=o();d.backlog=s},{}],gos:[function(e,t,n){function r(e,t,n){if(o.call(e,t))return e[t];var r=n();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(e,t,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return e[t]=r,r}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],handle:[function(e,t,n){function r(e,t,n,r){o.buffer([e],r),o.emit(e,t,n)}var o=e("ee").get("handle");t.exports=r,r.ee=o},{}],id:[function(e,t,n){function r(e){var t=typeof e;return!e||"object"!==t&&"function"!==t?-1:e===window?0:a(e,i,function(){return o++})}var o=1,i="nr@id",a=e("gos");t.exports=r},{}],loader:[function(e,t,n){function r(){if(!x++){var e=h.info=NREUM.info,t=d.getElementsByTagName("script")[0];if(setTimeout(s.abort,3e4),!(e&&e.licenseKey&&e.applicationID&&t))return s.abort();f(y,function(t,n){e[t]||(e[t]=n)}),c("mark",["onload",a()+h.offset],null,"api");var n=d.createElement("script");n.src="https://"+e.agent,t.parentNode.insertBefore(n,t)}}function o(){"complete"===d.readyState&&i()}function i(){c("mark",["domContent",a()+h.offset],null,"api")}function a(){return E.exists&&performance.now?Math.round(performance.now()):(u=Math.max((new Date).getTime(),u))-h.offset}var u=(new Date).getTime(),c=e("handle"),f=e(2),s=e("ee"),p=window,d=p.document,l="addEventListener",m="attachEvent",w=p.XMLHttpRequest,g=w&&w.prototype;NREUM.o={ST:setTimeout,SI:p.setImmediate,CT:clearTimeout,XHR:w,REQ:p.Request,EV:p.Event,PR:p.Promise,MO:p.MutationObserver};var v=""+location,y={beacon:"bam.nr-data.net",errorBeacon:"bam.nr-data.net",agent:"js-agent.newrelic.com/nr-1071.min.js"},b=w&&g&&g[l]&&!/CriOS/.test(navigator.userAgent),h=t.exports={offset:u,now:a,origin:v,features:{},xhrWrappable:b};e(1),d[l]?(d[l]("DOMContentLoaded",i,!1),p[l]("load",r,!1)):(d[m]("onreadystatechange",o),p[m]("onload",r)),c("mark",["firstbyte",u],null,"api");var x=0,E=e(4)},{}]},{},["loader"]);</script>
  <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript" src="//thingspeak.com/highcharts-3.0.8.js"></script>
  <script src="/assets/application-03e2c06ff0f3b0568246067907657e6d.js"></script>
    <script type="text/javascript">
    // user's timezone offset
    var myOffset = new Date().getTimezoneOffset();

    // converts date format from JSON
    function getChartDate(d) {
      // offset in minutes is converted to milliseconds and subtracted so that chart's x-axis is correct
      return Date.parse(d) - (myOffset * 60000);
    }

    \$(document).on('page:load ready', function() {
      // blank array for holding chart data
      var chartData = [];
      // variable for the local date in milliseconds
      var localDate;
      // variable for the last date added to the chart
      var last_date;

      // get the data with a webservice call
      \$.getJSON('https://thingspeak.com/channels/${channel}/field/${humiField}.json?callback=?&amp;offset=0&amp;days=7&amp;timescale=240', function(data) {

          // if no access
          if (data == '-1') {
            \$('#chart-container').append('This channel is not public.  To embed charts, the channel must be public or a read key must be specified.');
          }

          // iterate through each feed
          \$.each(data.feeds, function() {
            var p = new Highcharts.Point();
            // set the proper values
            var v = this.field${humiField};
            p.x = getChartDate(this.created_at);
            p.y = parseFloat(v);
            // add location if possible
            if (this.location) {
              p.name = this.location;
            }
            // if a numerical value exists add it
            if (!isNaN(parseInt(v))
                            ) {
              chartData.push(p);
            }
          });

          // specify the chart options
          var chartOptions = {
            chart: {
              renderTo: 'chart-container',
              defaultSeriesType: 'line',
              backgroundColor: '#ffffff',
              style: {
                fontSize: '15pt',
                fontWeight: 'bold'
              },              
              events: {
                load: function() {
                  //if dynamic and no "timeslice" options are set
                  //   GAK 02/16/2013 Let's try to add the last "average" slice if params[:average]

                  var url = 'https://thingspeak.com/channels/${channel}/feed/last.json?callback=?&amp;offset=0&amp;location=true&amp;days=7&amp;timescale=240';
                  if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_average.json?callback=?&amp;offset=0&amp;location=true&amp;average=&amp;days=7&amp;timescale=240';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_median.json?callback=?&amp;offset=0&amp;location=true&amp;median=&amp;days=7&amp;timescale=240';
                  } else if ("".length > 0) {
                    url = 'https://thingspeak.com/channels/${channel}/feed/last_sum.json?callback=?&amp;offset=0&amp;location=true&amp;sum=&amp;days=7&amp;timescale=240';
                  }

                  if ('true' === 'true' && ('30'.length < 1)) {
                    // push data every 15 seconds
                    setInterval(function() {
                      // get the data with a webservice call if we're just getting the last channel
                      \$.getJSON(url, function(data) {
                        // if data exists
                        if (data && data.field${humiField}) {

                          var p = new Highcharts.Point();
                          // set the proper values
                          var v = data.field${humiField};

                          p.x = getChartDate(data.created_at);
                          p.y = parseFloat(v);
                          // add location if possible
                          if (data.location) {
                            p.name = data.location;
                          }
                          // get the last date if possible
                          if (dynamicChart.series[0].data.length > 0) {
                            last_date = dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].x;
                          }
                          var shift = false; //default for shift

                          //if push is requested in parameters
                          // then if results is and data.length is < results, shift = false
                          var results = 360;

                          if (results && dynamicChart.series[0].data.length + 1 >= results) {
                            shift = true;
                          }
                          // if a numerical value exists and it is a new date, add it
                          if (!isNaN(parseInt(v)) && (p.x != last_date)
                                                        ) {
                            dynamicChart.series[0].addPoint(p, true, shift);
                          } else {
                            dynamicChart.series[0].data[dynamicChart.series[0].data.length - 1].update(p);
                          }
                        }
                      });

                    }, 15000);
                  }
                }
              }
            },
            title: {
              text: '',
              style: {
                fontSize: '20pt',
                color: '#0003ce',
                fontWeight: 'bold'
              }
            },
            plotOptions: {
              line: {
                color: '#018ec1'
              },
              bar: {
                color: '#018ec1'
              },
              column: {
                color: '#018ec1'
              },
              spline: {
                color: '#018ec1'
              },
              series: {
                marker: {
                  radius: 3
                },
                animation: true,
                step: false,
                borderWidth: 0,
                turboThreshold: 0
              }
            },
            exporting: {
              enabled: false
            },
            tooltip: {
              // reformat the tooltips so that local times are displayed
              formatter: function() {
                var d = new Date(this.x + (myOffset * 60000));
                var n = (this.point.name === undefined) ? '' : '<br/>' + this.point.name;
                return this.series.name + ':<b>' + this.y + '</b>' + n + '<br/>' + d.toDateString() + '<br/>' + d.toTimeString().replace();
              }
            },
            xAxis: {
              type: 'datetime',
              title: {
                text: 'test',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              }
            },
            yAxis: {
              title: {
                text: '',
                style: {
                fontSize: '17pt',
                color: '#7a7a7a',
                fontWeight: 'bold'
              }
              },
              min: null ,
              max: null
            },
            legend: {
              enabled: false
            },
            series: [{
              name: data.channel.field${humiField}
            }],
            credits: {
              text: 'ThingSpeak.com',
              href: 'https://thingspeak.com/',
              style: {
                color: '#D62020'
              }
            }
          };

          // add the data to the chart
          chartOptions.series[0].data = chartData;

          // set chart labels here so that decoding occurs properly
          chartOptions.title.text = decodeURIComponent('Humidity%20-%20Week');
          chartOptions.xAxis.title.text = decodeURIComponent('time');
          chartOptions.yAxis.title.text = decodeURIComponent('%25');

          // draw the chart
          var dynamicChart = new Highcharts.Chart(chartOptions);

          // end getJSON success
        })
        // chained to getjson, on error
        .error(function() {
          \$('#chart-container').html('Invalid Channel.');
        });

    }); // end document.ready
    </script>
</head>

<body style='background-color: #ffffff; height: 100%; margin: 0; padding: 0;'>
  <div id="chart-container" style="width: 945px; height: 450px; display: block; position:absolute; bottom:0; top:0; left:0; right:0; margin: 5px 15px 15px 0; overflow: hidden;">
    <img alt="Loader transparent" src="/assets/loader-transparent-dcfdb186859b297e13cf2c077ca08e36.gif" style="position: absolute; margin: auto; top: 0; left: 0; right: 0; bottom: 0;" />
  </div>
</body>

</html>


"""
 render contentType: "text/html", data: html, status: 200
}




def whitelist() {
    ["code.highcharts.com",
    "ajax.googleapis.com"]
}
//--------------------------------------




// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def setInfo(String app_url, String id) {
	log.debug "${app_url}, ${id}"
	state.app_url = app_url
    state.id = id
}

def setStatus(params){
    log.debug "${params.key} : ${params.data}"
 //    def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
//    sendEvent(name: "lastCheckin", value: now)
//    def now = formatDate()
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)

	// Any report - temp, humidity, pressure, & battery - results in a lastCheckin event and update to Last Checkin tile
	// However, only a non-parseable report results in lastCheckin being displayed in events log
    sendEvent(name: "lastCheckin", value: now, displayed: false)

	// Check if the min/max temp and min/max humidity should be reset
    
 	switch(params.key){
    case "relativeHumidity":
		state.apihumi = params.data
		pollhumi()
		def para = "${params.data}"
		String data = para
		def stf = Float.parseFloat(data)
		def humidity = Math.round(stf)
    	sendEvent(name:"humidity", value: humidity )
        updateMinMaxHumidity(humidity)
    	break;
    case "temperature":
		def para = "${params.data}"
		String data = para
		def st = data.replace("C","");
		def stf = Float.parseFloat(st)
		def tem = Math.round(stf*10)/10
        sendEvent(name:"temperature", value: tem )
		state.apitemp = "${tem}"
		polltemp()        
        updateMinMaxTemps(tem)
	    checkNewDay()	
//        log.debug "${st}"
    	break;
    case "atmosphericPressure":
    	sendEvent(name:"pressure", value: params.data.replace(" Pa","").replace(",","").toInteger()/1000 )
    	break;
    case "batteryLevel":
    	sendEvent(name:"battery", value: params.data )
    	break;				
    }
}

//${tempField}
def polltemp() {
    def url = "https://api.thingspeak.com/update?key=${apiKey}&field${tempField}=${state.apitemp}"
    httpGet(url) { 
        response -> 
        if (response.status != 200 ) {
            log.debug "ThingSpeak logging failed, status = ${response.status}"
        }
    }
    def refreshTime = (refreshRateMin as int) * 60
    	runIn(refreshTime, polltemp)        
        log.debug "Update Temperature to ThingSpeak = ${state.apitemp}C"
}    
/*    if (state.accessKey && state.apitemp) {
        def params = [
    	    uri: "https://api.thingspeak.com/update?api_key=${state.accessKey}&field1=${state.aiptemp}"
    	]
        try {
		httpGet(params)}catch (e) {
            log.error "error: $e"
        }
    }
}*/

def pollhumi() {
    def url = "https://api.thingspeak.com/update?key=${apiKey}&field${humiField}=${state.apihumi}"
    httpGet(url) { 
        response -> 
        if (response.status != 200 ) {
            log.debug "ThingSpeak logging failed, status = ${response.status}"
        }
    }
    def refreshTime = (refreshRateMin as int) * 60
	    runIn(refreshTime, pollhumi)
        log.debug "Update Humidity to ThingSpeak = ${state.apihumi}%"
}    
    
/*    if (state.accessKey && state.apihumi) {
        def params = [
    	    uri: "https://api.thingspeak.com/update?api_key=${state.accessKey}&field2=${state.aiptemp}"
    	]
        try {
		httpGet(params)}catch (e) {
            log.error "error: $e"
        }
    }
}*/
def updated() {
    setLanguage(settings.selectedLang)
    polltemp()
    pollhumi()
    
    if(state.sunMaxTemp == null) state.sunMaxTemp = 0
    if(state.sunMinTemp == null) state.sunMinTemp = 0
    if(state.sunMaxHumi == null) state.sunMaxHumi = 0
    if(state.sunMinHumi == null) state.sunMinHumi = 0
    
    if(state.monMaxTemp == null) state.monMaxTemp = 0
    if(state.monMinTemp == null) state.monMinTemp = 0
    if(state.monMaxHumi == null) state.monMaxHumi = 0
    if(state.monMinHumi == null) state.monMinHumi = 0
	
    if(state.tueMaxTemp == null) state.tueMaxTemp = 0
    if(state.tueMinTemp == null) state.tueMinTemp = 0
    if(state.tueMaxHumi == null) state.tueMaxHumi = 0
    if(state.tueMinHumi == null) state.tueMinHumi = 0
    
    if(state.wedMaxTemp == null) state.wedMaxTemp = 0
    if(state.wedMinTemp == null) state.wedMinTemp = 0
    if(state.wedMaxHumi == null) state.wedMaxHumi = 0
    if(state.wedMinHumi == null) state.wedMinHumi = 0
    
    if(state.thuMaxTemp == null) state.thuMaxTemp = 0
    if(state.thuMinTemp == null) state.thuMinTemp = 0
    if(state.thuMaxHumi == null) state.thuMaxHumi = 0
    if(state.thuMinHumi == null) state.thuMinHumi = 0

    if(state.friMaxTemp == null) state.friMaxTemp = 0
    if(state.friMinTemp == null) state.friMinTemp = 0
    if(state.friMaxHumi == null) state.friMaxHumi = 0
    if(state.friMinHumi == null) state.friMinHumi = 0
    
    if(state.satMaxTemp == null) state.satMaxTemp = 0
    if(state.satMinTemp == null) state.satMinTemp = 0
    if(state.satMaxHumi == null) state.satMaxHumi = 0
    if(state.satMinHumi == null) state.satMinHumi = 0
}

def setLanguage(language){
    log.debug "Languge >> ${language}"
	state.language = language
    
    sendEvent(name:"pre", value: LANGUAGE_MAP["atmospheric_pressure"][language] )
    sendEvent(name:"humi", value: LANGUAGE_MAP["humidity"][language] )
    sendEvent(name:"bat", value: LANGUAGE_MAP["battery"][language] )
    
    refreshMultiAttributes()
}

def checkNewDay() {
	def now = new Date().format("yyyy-MM-dd", location.timeZone)
	if(state.prvDate == null){
		state.prvDate = now
	}else{
		if(state.prvDate != now){
			state.prvDate = now
			    log.debug "checkNewDay _ ${state.prvDate}"
			resetMinMax()            
		}
	}
}

def resetMinMax() {
	def day = new Date().format("EEE", location.timeZone)
	log.debug "resetMinMax day_ ${day}"
	def currentMaxTemp = device.currentValue('maxTemp')
	def currentMinTemp = device.currentValue('minTemp')
	def currentMaxHumi = device.currentValue('maxHumidity')
	def currentMinHumi = device.currentValue('minHumidity')	
	log.debug "resetMinMax day_ ${day}_xT${currentMaxTemp}_nT${currentMinTemp}_xH${currentMaxHumi}_nH${currentMinHumi}"
	
	if (day == "Mon") {
		 state.sunMaxTemp = currentMaxTemp
		 state.sunMinTemp = currentMinTemp
		 state.sunMaxHumi = currentMaxHumi
		 state.sunMinHumi = currentMinHumi
	} else if (day == "Tue") {
		 state.monMaxTemp = currentMaxTemp
		 state.monMinTemp = currentMinTemp
		 state.monMaxHumi = currentMaxHumi
		 state.monMinHumi = currentMinHumi
	} else if (day == "Wed") {
		 state.tueMaxTemp = currentMaxTemp
		 state.tueMinTemp = currentMinTemp
		 state.tueMaxHumi = currentMaxHumi
		 state.tueMinHumi = currentMinHumi
	} else if (day == "Thu") {
		 state.wedMaxTemp = currentMaxTemp
		 state.wedMinTemp = currentMinTemp
		 state.wedMaxHumi = currentMaxHumi
		 state.wedMinHumi = currentMinHumi
	} else if (day == "Fri") {
		 state.thuMaxTemp = currentMaxTemp
		 state.thuMinTemp = currentMinTemp
		 state.thuMaxHumi = currentMaxHumi
		 state.thuMinHumi = currentMinHumi
	} else if (day == "Sat") {
		 state.friMaxTemp = currentMaxTemp
		 state.friMinTemp = currentMinTemp
		 state.friMaxHumi = currentMaxHumi
		 state.friMinHumi = currentMinHumi
	} else if (day == "Sun") {
		 state.satMaxTemp = currentMaxTemp
		 state.satMinTemp = currentMinTemp
		 state.satMaxHumi = currentMaxHumi
		 state.satMinHumi = currentMinHumi
	}
	
	def currentTemp = device.currentValue('temperature')
	def currentHumidity = device.currentValue('humidity')
    currentTemp = currentTemp ? (int) currentTemp : currentTemp
	log.debug "${device.displayName}: Resetting daily min/max values to current temperature of ${currentTemp}° and humidity of ${currentHumidity}%"
    sendEvent(name: "maxTemp", value: currentTemp, displayed: false)
    sendEvent(name: "minTemp", value: currentTemp, displayed: false)
    sendEvent(name: "maxHumidity", value: currentHumidity, displayed: false)
    sendEvent(name: "minHumidity", value: currentHumidity, displayed: false)
    refreshMultiAttributes()
}

// Check new min or max temp for the day
def updateMinMaxTemps(temp) {
//	temp = temp ? (int) temp : temp
	if ((temp > device.currentValue('maxTemp')) || (device.currentValue('maxTemp') == null))
		sendEvent(name: "maxTemp", value: temp, displayed: false)	
	if ((temp < device.currentValue('minTemp')) || (device.currentValue('minTemp') == null))
		sendEvent(name: "minTemp", value: temp, displayed: false)
	refreshMultiAttributes()
}

// Check new min or max humidity for the day
def updateMinMaxHumidity(humidity) {
	if ((humidity > device.currentValue('maxHumidity')) || (device.currentValue('maxHumidity') == null))
		sendEvent(name: "maxHumidity", value: humidity, displayed: false)
	if ((humidity < device.currentValue('minHumidity')) || (device.currentValue('minHumidity') == null))
		sendEvent(name: "minHumidity", value: humidity, displayed: false)
	refreshMultiAttributes()
}

// Update display of multiattributes in main tile
def refreshMultiAttributes() {
	def day = new Date().format("EEE", location.timeZone)		
    
	def temphiloAttributes = displayTempHighLow ? (displayHumidHighLow ? getWordByLang("todays") + " " + getWordByLang("high") + "/" + getWordByLang("low") + ":  ${device.currentState('maxTemp')?.value}° / ${device.currentState('minTemp')?.value}°" : getWordByLang("todays") + " " + getWordByLang("high") + ": ${device.currentState('maxTemp')?.value}°  /  " + getWordByLang("low") + ": ${device.currentState('minTemp')?.value}°") : ""
	def humidhiloAttributes = displayHumidHighLow ? (displayTempHighLow ? "    ${device.currentState('maxHumidity')?.value}% / ${device.currentState('minHumidity')?.value}%" : getWordByLang("todays") + " " + getWordByLang("high") + ": ${device.currentState('maxHumidity')?.value}%  /  " + getWordByLang("low") + ": ${device.currentState('minHumidity')?.value}%") : ""
	sendEvent(name: "multiAttributesReport", value: "${temphiloAttributes}${humidhiloAttributes}", displayed: false)
	if (day == "Mon") {
		sendEvent(name: "1l", value: getWordByLang("tue"))
		sendEvent(name: "2l", value: getWordByLang("wed"))
		sendEvent(name: "3l", value: getWordByLang("thu"))
		sendEvent(name: "4l", value: getWordByLang("fri"))
		sendEvent(name: "5l", value: getWordByLang("sat"))
		sendEvent(name: "6l", value: getWordByLang("sun"))
		sendEvent(name: "1t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "2t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "3t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "4t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "5t", value: state.satMaxTemp + "°\n" + state.satMinTemp + "°")
		sendEvent(name: "6t", value: state.sunMaxTemp + "°\n" + state.sunMinTemp + "°")
		sendEvent(name: "1h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "2h", value: state.wedMaxHumi + "%\n" + state.wedMinHumi + "%")
		sendEvent(name: "3h", value: state.thuMaxHumi + "%\n" + state.thuMinHumi + "%")
		sendEvent(name: "4h", value: state.friMaxHumi + "%\n" + state.friMinHumi + "%")
		sendEvent(name: "5h", value: state.satMaxHumi + "%\n" + state.satMinHumi + "%")
		sendEvent(name: "6h", value: state.sunMaxHumi + "%\n" + state.sunMinHumi + "%")
	} else if (day == "Tue") {
		sendEvent(name: "6l", value: getWordByLang("mon"))
		sendEvent(name: "1l", value: getWordByLang("wed"))
		sendEvent(name: "2l", value: getWordByLang("thu"))
		sendEvent(name: "3l", value: getWordByLang("fri"))
		sendEvent(name: "4l", value: getWordByLang("sat"))
		sendEvent(name: "5l", value: getWordByLang("sun"))
		sendEvent(name: "6t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "1t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "2t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "3t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "4t", value: state.satMaxTemp + "\n" + state.satMinTemp + "°")
		sendEvent(name: "5t", value: state.sunMaxTemp + "\n" + state.sunMinTemp + "°")
		sendEvent(name: "6h", value: state.tueMaxHumi + "\n" + state.tueMinHumi + "%")
		sendEvent(name: "1h", value: state.wedMaxHumi + "\n" + state.wedMinHumi + "%")
		sendEvent(name: "2h", value: state.thuMaxHumi + "\n" + state.thuMinHumi + "%")
		sendEvent(name: "3h", value: state.friMaxHumi + "\n" + state.friMinHumi + "%")
		sendEvent(name: "4h", value: state.satMaxHumi + "\n" + state.satMinHumi + "%")
		sendEvent(name: "5h", value: state.sunMaxHumi + "\n" + state.sunMinHumi + "%")
	} else if (day == "Wed") {
		sendEvent(name: "6l", value: getWordByLang("tue"))
		sendEvent(name: "5l", value: getWordByLang("mon"))
		sendEvent(name: "l",  value: getWordByLang("tue"))
		sendEvent(name: "2l", value: getWordByLang("fri"))
		sendEvent(name: "3l", value: getWordByLang("sat"))
		sendEvent(name: "4l", value: getWordByLang("sun"))
		sendEvent(name: "6t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "5t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "1t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "2t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "3t", value: state.satMaxTemp + "°\n" + state.satMinTemp + "°")
		sendEvent(name: "4t", value: state.sunMaxTemp + "%°\n" + state.sunMinTemp + "°")
		sendEvent(name: "6h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "5h", value: state.monMaxHumi + "%\n" + state.monMinHumi + "%")
		sendEvent(name: "1h", value: state.thuMaxHumi + "%\n" + state.thuMinHumi + "%")
		sendEvent(name: "2h", value: state.friMaxHumi + "%\n" + state.friMinHumi + "%")
		sendEvent(name: "3h", value: state.satMaxHumi + "%\n" + state.satMinHumi + "%")
		sendEvent(name: "4h", value: state.sunMaxHumi + "%\n" + state.sunMinHumi + "%")
	} else if (day == "Thu") {
		sendEvent(name: "5l", value: getWordByLang("tue"))
		sendEvent(name: "6l", value: getWordByLang("wed"))
		sendEvent(name: "4l", value: getWordByLang("mon"))
		sendEvent(name: "1l", value: getWordByLang("fri"))
		sendEvent(name: "2l", value: getWordByLang("sat"))
		sendEvent(name: "3l", value: getWordByLang("sun"))
		sendEvent(name: "5t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "6t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "4t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "1t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "2t", value: state.satMaxTemp + "°\n" + state.satMinTemp + "°")
		sendEvent(name: "3t", value: state.sunMaxTemp + "°\n" + state.sunMinTemp + "°")
		sendEvent(name: "5h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "6h", value: state.wedMaxHumi + "%\n" + state.wedMinHumi + "%")
		sendEvent(name: "4h", value: state.monMaxHumi + "%\n" + state.monMinHumi + "%")
		sendEvent(name: "1h", value: state.friMaxHumi + "%\n" + state.friMinHumi + "%")
		sendEvent(name: "2h", value: state.satMaxHumi + "%\n" + state.satMinHumi + "%")
		sendEvent(name: "3h", value: state.sunMaxHumi + "%\n" + state.sunMinHumi + "%")
	} else if (day == "Fri") {
		sendEvent(name: "4l", value: getWordByLang("tue"))
		sendEvent(name: "5l", value: getWordByLang("wed"))
		sendEvent(name: "6l", value: getWordByLang("thu"))
		sendEvent(name: "3l", value: getWordByLang("mon"))
		sendEvent(name: "1l", value: getWordByLang("sat"))
		sendEvent(name: "2l", value: getWordByLang("sun"))
		sendEvent(name: "4t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "5t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "6t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "3t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "1t", value: state.satMaxTemp + "°\n" + state.satMinTemp + "°")
		sendEvent(name: "2t", value: state.sunMaxTemp + "°\n" + state.sunMinTemp + "°")
		sendEvent(name: "4h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "5h", value: state.wedMaxHumi + "%\n" + state.wedMinHumi + "%")
		sendEvent(name: "6h", value: state.thuMaxHumi + "%\n" + state.thuMinHumi + "%")
		sendEvent(name: "3h", value: state.monMaxHumi + "%\n" + state.monMinHumi + "%")
		sendEvent(name: "1h", value: state.satMaxHumi + "%\n" + state.satMinHumi + "%")
		sendEvent(name: "2h", value: state.sunMaxHumi + "%\n" + state.sunMinHumi + "%")
	} else if (day == "Sat") {
		sendEvent(name: "3l", value: getWordByLang("tue"))
		sendEvent(name: "4l", value: getWordByLang("wed"))
		sendEvent(name: "5l", value: getWordByLang("thu"))
		sendEvent(name: "6l", value: getWordByLang("fri"))
		sendEvent(name: "2l", value: getWordByLang("mon"))
		sendEvent(name: "1l", value: getWordByLang("sun"))
		sendEvent(name: "3t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "4t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "5t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "6t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "2t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "1t", value: state.sunMaxTemp + "°\n" + state.sunMinTemp + "°")
		sendEvent(name: "3h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "4h", value: state.wedMaxHumi + "%\n" + state.wedMinHumi + "%")
		sendEvent(name: "5h", value: state.thuMaxHumi + "%\n" + state.thuMinHumi + "%")
		sendEvent(name: "6h", value: state.friMaxHumi + "%\n" + state.friMinHumi + "%")
		sendEvent(name: "2h", value: state.monMaxHumi + "%\n" + state.monMinHumi + "%")
		sendEvent(name: "1h", value: state.sunMaxHumi + "%\n" + state.sunMinHumi + "%")
	} else if (day == "Sun") {
		sendEvent(name: "2l", value: getWordByLang("tue"))
		sendEvent(name: "3l", value: getWordByLang("wed"))
		sendEvent(name: "4l", value: getWordByLang("thu"))
		sendEvent(name: "5l", value: getWordByLang("fri"))
		sendEvent(name: "6l", value: getWordByLang("sat"))
		sendEvent(name: "1l", value: getWordByLang("mon"))
		sendEvent(name: "2t", value: state.tueMaxTemp + "°\n" + state.tueMinTemp + "°")
		sendEvent(name: "3t", value: state.wedMaxTemp + "°\n" + state.wedMinTemp + "°")
		sendEvent(name: "4t", value: state.thuMaxTemp + "°\n" + state.thuMinTemp + "°")
		sendEvent(name: "5t", value: state.friMaxTemp + "°\n" + state.friMinTemp + "°")
		sendEvent(name: "6t", value: state.satMaxTemp + "°\n" + state.satMinTemp + "°")
		sendEvent(name: "1t", value: state.monMaxTemp + "°\n" + state.monMinTemp + "°")
		sendEvent(name: "2h", value: state.tueMaxHumi + "%\n" + state.tueMinHumi + "%")
		sendEvent(name: "3h", value: state.wedMaxHumi + "%\n" + state.wedMinHumi + "%")
		sendEvent(name: "4h", value: state.thuMaxHumi + "%\n" + state.thuMinHumi + "%")
		sendEvent(name: "5h", value: state.friMaxHumi + "%\n" + state.friMinHumi + "%")
		sendEvent(name: "6h", value: state.satMaxHumi + "%\n" + state.satMinHumi + "%")
		sendEvent(name: "1h", value: state.monMaxHumi + "%\n" + state.monMinHumi + "%")
	} 
		
}

def refresh(){
	log.debug "Refresh"
    def options = [
     	"method": "GET",
        "path": "/devices/get/${state.id}",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ]
    ]
    sendCommand(options, callback)
}

def sendCommand(options, _callback){
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)
        log.debug jsonObj
        state.apitemp = jsonObj.properties.temperature.value
        state.apihumi = jsonObj.properties.relativeHumidity
        
 		sendEvent(name:"battery", value: jsonObj.properties.batteryLevel)
        sendEvent(name:"temperature", value: jsonObj.properties.temperature.value)
        sendEvent(name:"humidity", value: jsonObj.properties.relativeHumidity)
        sendEvent(name:"pressure", value: jsonObj.properties.atmosphericPressure.value / 1000)
        polltemp()
        pollhumi()
        updateLastTime()
        checkNewDay()
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now)
}

def getWordByLang(id){
	return LANGUAGE_MAP[id][state.language]
}