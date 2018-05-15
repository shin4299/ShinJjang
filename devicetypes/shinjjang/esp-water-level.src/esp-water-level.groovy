/**
 *  ESP Easy DTH (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 
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


metadata {
	definition (name: "ESP Water Level", namespace: "ShinJjang", author: "ShinJjangwithFison67") {
		capability "Temperature Measurement"
        capability "Sensor"
        capability "Carbon Dioxide Measurement"
        capability "Refresh"
        
		attribute "maxTemp", "number"
		attribute "minTemp", "number"
		attribute "maxCo", "number"
		attribute "minCo", "number"
        attribute "lastCheckinDate", "date"
        
        command "setData"
        command "refresh"
        command "timerLoop"
		command	"checkNewDay"

	}


	simulator {
	}
    preferences {
		input "url", "text", title: "ESP IP주소", description: "로컬IP 주소를 입력", required: true
        input "refreshRateMin", "enum", title: "Update interval", defaultValue: 60, options:[5: "5 sec", 10: "10 sec", 30 : "30 sec", 60: "1 min", 180 :"3 min", 360: "6 min", 720: "12 min", 1440: "24 min"], displayDuringSetup: true
		input "apiKey", "text", type: "password", title: "Write API Key", description: "thingspeak.com에서 계정을 만들고 채널을 생성하세요.", required: true 
		input "channel", "text", title: "Channel ID", description: "이산화탄소 농도를 기록할 공개채널ID", required: true
		input name:"coField", type:"number", title:"Field Number", description:"이산화탄소 농도를 기록할 필드(Field)", required: true
        input "updatecycle", "enum", title: "업데이트 주기", defaultValue: 5, options:[1: "1Min", 2: "2Min", 5: "5Min", 10: "10Min", 15 : "15Min", 20: "20Min", 30: "30Min"], description: "농도 데이터 업데이트 주기", displayDuringSetup: true        
        input "howdays", "enum", title: "두번째 그래프 표시일수", defaultValue: 7, options:[3: "3 Days", 7: "7 Days", 15: "15 Days", 30: "30 Days"], description: "두번째 그래프에 불러올 데이터 날수", displayDuringSetup: true        
    }

	tiles(scale: 2) {
    		multiAttributeTile(name:"main", type:"generic", width:6, height:4) {
			tileAttribute("device.carbonDioxide", key: "PRIMARY_CONTROL") {
            	attributeState "carbonDioxide",label:'${currentValue}', backgroundColors:[
 				[value: 400, color: "#a3da91"],
 				[value: 600, color: "#d8e288"],
                [value: 999, color: "#f2d33c"],
                [value: 1500, color: "#dd6637"],
                [value: 2500, color: "#77110b"]
				]
            }
            tileAttribute ("device.lastCheckin", key: "SECONDARY_CONTROL") {
				attributeState "lastCheckin", label:'Updated: ${currentValue}'
			}
		} 
        valueTile("list", "device.carbonDioxide") {
            state "carbonDioxide",label:'${currentValue}ppm', icon:"https://postfiles.pstatic.net/MjAxODA0MThfOCAg/MDAxNTIzOTk1MzE3MjY2.cDoiO_gS9Kruy61OR8Ek_TKcph10ygy2_-rwzJAs-aUg.1zZ_dy3o2kYf9aMXFNoTytnX5opVUl5Ut4yOL7NmPQQg.PNG.shin4299/planet_%281%29.png?type=w580", backgroundColors:[
 				[value: 400, color: "#a3da91"],
 				[value: 600, color: "#d8e288"],
                [value: 999, color: "#f2d33c"],
                [value: 1500, color: "#dd6637"],
                [value: 2500, color: "#77110b"]
				]
            }
        valueTile("temperature", "device.temperature", width:2, height:2) {
            state "temperature", label:'${currentValue}°', defaultState: true,
            backgroundColors:[
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
            ]
        }        
        valueTile("temp_label", "", decoration: "flat", width:2, height:1) {
            state "default", label:'현재온도'
        }
        valueTile("refresh_label", "", decoration: "flat", width:2, height:1) {
            state "default", label:'새로고침'
        }
        valueTile("tempXN_label", "", decoration: "flat", width:4, height:1) {
            state "default", label:'오늘 온도변화'
        }
        valueTile("maxTemp", "device.maxTemp", decoration: "flat", width:2, height:1) {
            state "default", label:'최고 ${currentValue}°'
        }
        valueTile("maxTempTime", "device.maxTempTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("minTemp", "device.minTemp", decoration: "flat", width:2, height:1) {
            state "default", label:'최저 ${currentValue}°'
        }
        valueTile("minTempTime", "device.minTempTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("COXN_label", "", decoration: "flat", width:4, height:1) {
            state "default", label:'오늘 CO2변화'
        }
        valueTile("maxCo", "device.maxCo", decoration: "flat", width:2, height:1) {
            state "default", label:'최고 ${currentValue}ppm'
        }
        valueTile("maxCoTime", "device.maxCoTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("minCo", "device.minCo", decoration: "flat", width:2, height:1) {
            state "default", label:'최저 ${currentValue}ppm'
        }
        valueTile("minCoTime", "device.minCoTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        standardTile("refresh", "device.refresh", width:2, height:2) {
            state "default", label:"", action:"refresh", icon:"st.secondary.refresh", backgroundColor:"#A7ADBA"
        }
        htmlTile(name:"CoD",action:"CoD", type: "HTML",width: 6, height: 6, whitelist: whitelist())
        htmlTile(name:"CoW",action:"CoW", type: "HTML",width: 6, height: 6, whitelist: whitelist())
       
       	main (["list"])
      	details(["main", "temp_label","tempXN_label", 
        		 "temperature", "maxTemp", "maxTempTime", "minTemp", "minTempTime",
                 "refresh_label", "COXN_label", 
                 "refresh", "maxCo", "maxCoTime",  "minCo", "minCoTime",
                 "CoD", "CoW"])
	}
}
mappings {
	path("/CoD") { action: [GET:"CoD"] }
	path("/CoW") { action: [GET:"CoW"] }
}

def CoD() {
	def html = """

<html ng-app="embedinsapp" lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta property="og:title" content="WaterTank"/>
    <meta property="og:type" content="article"/>
    <meta property="og:description" content="Check out my sensor data WaterTank"/>
    <meta property="og:url" content="https://app.ubidots.com/ubi/getchart/page/u4Bsn_7OU4b4M8Ev-u-ORHZ-Kec at ubidots.com"/>
    <meta property="og:image"
          content="https://app.ubidots.comhttps://648d4.https.cdn.softlayer.net/app/static/images/logos/ubidots_big.png"/>
    <meta property="og:site_name" content="Ubidots"/>

    <title>WaterTank</title>

    <link rel="stylesheet" href="https://648d4.https.cdn.softlayer.net/app/static/CACHE/css/daad6cb64ff6.css" type="text/css" />
    <link href='//fonts.googleapis.com/css?family=Open+Sans:700,600,400,300' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="https://648d4.https.cdn.softlayer.net/app/static/js/libs/nvd3/1.8.5/nv.d3.min.css">

    <style type="text/css">
        html, body { overflow: hidden; }
    </style>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/jstimezonedetect/jstz-1.0.4.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/d3.min.js"></script>
    <script type="text/javascript" src="//maps.google.com/maps/api/js?v=3.28&key=AIzaSyCtOe1RtUgmdOK1ofAyjJEJlMcizAoYnx0"></script>
    <script type="text/javascript">
        var ubi_tz="";
        var staticUrl = 'https://648d4.https.cdn.softlayer.net/app/static/';
        var isSingleUserSharedInstance = false;
        \$().ready(function(){
            
            ubi_tz = jstz.determine().name();
            
            moment.tz.setDefault(ubi_tz);
        });
        var permissions = 1;
    </script>

    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/CACHE/js/6c9259b9387d.js"></script>

    <!-- End / Insight Directives -->
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/socket.io/socket.io.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/bootstrap-colorpicker.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/slider/js/rzslider.min.js"></script>

    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/angular-1.5.8/angular-sanitize.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/ng-csv.min.js"></script>
    <script type="text/javascript" >
        UbiModelsApp.Constants.constant('UBI_APP_URL', 'https://app.ubidots.com/');
    </script>

    
    <script type="text/javascript">
        var mixpanel = {track: function(){}};
    </script>
    

    <style>
    .ins-widget [insights-info-box] {
        max-height: 400px;
    }
    </style>
    
    
    
     <div class="embed-insight-container" ng-controller="embedInsDisplay">
        
        <div ng-show="!outOfCredits && !trialExpired" class="embed-insight">
        
            
            <embed-insight getins="getInsByCode('u4Bsn_7OU4b4M8Ev-u-ORHZ-Kec')" get-data="embedGetData(insight)" page="true" out-of-credits="outOfCredits" trial-expired="trialExpired"></embed-insight>
            
        </div>

        
        <div ng-show="outOfCredits" ng-cloak style="flex: 1">
            <div class="row-fluid col-md-10 col-md-offset-1 col-xs-12 text-center information-box">
                <h1>Account out of credits</h1>
                
                <p>Click <a href="/userdata/plans/">here</a> to purchase more credits.</p>
                
            </div>
        </div>

        <div ng-show="trialExpired" ng-cloak style="flex: 1">
            <h1 ng-bind="('business_account_deactivated' | translate)"></h1>
        </div>
        
    </div>


    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/CACHE/js/cd785acfec6d.js"></script>
    <script src="/wafflejs"></script>
</head>
<body class="dashboard-body">

</body>
</html>

"""
 render contentType: "text/html", data: html, status: 200
}

def CoW() {
	def html = """
<html ng-app="embedinsapp" lang="en" class="ng-scope"><head><style type="text/css">[uib-typeahead-popup].dropdown-menu{display:block;}</style><style type="text/css">.uib-time input{width:50px;}</style><style type="text/css">[uib-tooltip-popup].tooltip.top-left > .tooltip-arrow,[uib-tooltip-popup].tooltip.top-right > .tooltip-arrow,[uib-tooltip-popup].tooltip.bottom-left > .tooltip-arrow,[uib-tooltip-popup].tooltip.bottom-right > .tooltip-arrow,[uib-tooltip-popup].tooltip.left-top > .tooltip-arrow,[uib-tooltip-popup].tooltip.left-bottom > .tooltip-arrow,[uib-tooltip-popup].tooltip.right-top > .tooltip-arrow,[uib-tooltip-popup].tooltip.right-bottom > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.top-left > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.top-right > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.bottom-left > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.bottom-right > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.left-top > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.left-bottom > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.right-top > .tooltip-arrow,[uib-tooltip-html-popup].tooltip.right-bottom > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.top-left > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.top-right > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.bottom-left > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.bottom-right > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.left-top > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.left-bottom > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.right-top > .tooltip-arrow,[uib-tooltip-template-popup].tooltip.right-bottom > .tooltip-arrow,[uib-popover-popup].popover.top-left > .arrow,[uib-popover-popup].popover.top-right > .arrow,[uib-popover-popup].popover.bottom-left > .arrow,[uib-popover-popup].popover.bottom-right > .arrow,[uib-popover-popup].popover.left-top > .arrow,[uib-popover-popup].popover.left-bottom > .arrow,[uib-popover-popup].popover.right-top > .arrow,[uib-popover-popup].popover.right-bottom > .arrow,[uib-popover-html-popup].popover.top-left > .arrow,[uib-popover-html-popup].popover.top-right > .arrow,[uib-popover-html-popup].popover.bottom-left > .arrow,[uib-popover-html-popup].popover.bottom-right > .arrow,[uib-popover-html-popup].popover.left-top > .arrow,[uib-popover-html-popup].popover.left-bottom > .arrow,[uib-popover-html-popup].popover.right-top > .arrow,[uib-popover-html-popup].popover.right-bottom > .arrow,[uib-popover-template-popup].popover.top-left > .arrow,[uib-popover-template-popup].popover.top-right > .arrow,[uib-popover-template-popup].popover.bottom-left > .arrow,[uib-popover-template-popup].popover.bottom-right > .arrow,[uib-popover-template-popup].popover.left-top > .arrow,[uib-popover-template-popup].popover.left-bottom > .arrow,[uib-popover-template-popup].popover.right-top > .arrow,[uib-popover-template-popup].popover.right-bottom > .arrow{top:auto;bottom:auto;left:auto;right:auto;margin:0;}[uib-popover-popup].popover,[uib-popover-html-popup].popover,[uib-popover-template-popup].popover{display:block !important;}</style><style type="text/css">.uib-datepicker-popup.dropdown-menu{display:block;float:none;margin:0;}.uib-button-bar{padding:10px 9px 2px;}</style><style type="text/css">.uib-position-measure{display:block !important;visibility:hidden !important;position:absolute !important;top:-9999px !important;left:-9999px !important;}.uib-position-scrollbar-measure{position:absolute !important;top:-9999px !important;width:50px !important;height:50px !important;overflow:scroll !important;}.uib-position-body-scrollbar-measure{overflow:scroll !important;}</style><style type="text/css">.uib-datepicker .uib-title{width:100%;}.uib-day button,.uib-month button,.uib-year button{min-width:100%;}.uib-left,.uib-right{width:100%}</style><style type="text/css">@charset "UTF-8";[ng:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng:form{display:block;}.ng-animate-shim{visibility:hidden;}.ng-anchor{position:absolute;}</style>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta property="og:title" content="WaterTank">
    <meta property="og:type" content="article">
    <meta property="og:description" content="Check out my sensor data WaterTank">
    <meta property="og:url" content="https://app.ubidots.com/ubi/getchart/page/u4Bsn_7OU4b4M8Ev-u-ORHZ-Kec at ubidots.com">
    <meta property="og:image" content="https://app.ubidots.comhttps://648d4.https.cdn.softlayer.net/app/static/images/logos/ubidots_big.png">
    <meta property="og:site_name" content="Ubidots">

    <title>WaterTank</title>

    <link rel="stylesheet" href="https://648d4.https.cdn.softlayer.net/app/static/CACHE/css/daad6cb64ff6.css" type="text/css">
    <link href="//fonts.googleapis.com/css?family=Open+Sans:700,600,400,300" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="https://648d4.https.cdn.softlayer.net/app/static/js/libs/nvd3/1.8.5/nv.d3.min.css">

    <style type="text/css">
        html, body { overflow: hidden; }
    </style>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/jstimezonedetect/jstz-1.0.4.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/d3.min.js"></script>
    <script type="text/javascript" src="//maps.google.com/maps/api/js?v=3.28&amp;key=AIzaSyCtOe1RtUgmdOK1ofAyjJEJlMcizAoYnx0"></script>
    <script type="text/javascript">
        var ubi_tz="";
        var staticUrl = 'https://648d4.https.cdn.softlayer.net/app/static/';
        var isSingleUserSharedInstance = false;
        \$().ready(function(){
            
            ubi_tz = jstz.determine().name();
            
            moment.tz.setDefault(ubi_tz);
        });
        var permissions = 1;
    </script>

    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/CACHE/js/6c9259b9387d.js"></script>

    <!-- End / Insight Directives -->
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/socket.io/socket.io.min.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/bootstrap-colorpicker.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/slider/js/rzslider.min.js"></script>

    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/angular-1.5.8/angular-sanitize.js"></script>
    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/js/libs/ng-csv.min.js"></script>
    <script type="text/javascript">
        UbiModelsApp.Constants.constant('UBI_APP_URL', 'https://app.ubidots.com/');
    </script>

    
    <script type="text/javascript">
        var mixpanel = {track: function(){}};
    </script>
    

    <style>
    .ins-widget [insights-info-box] {
        max-height: 400px;
    }
    </style>
</head>

<body class="dashboard-body">
    





<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="row">
            
            <div class="col-xs-12 col-lg-3">
                
                <div class="navbar-header">
                    <div class="mui-menu" mui-button="dashboard-sidebar">
                        
                    </div>

                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#menu-collapse" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a href="/" class="navbar-brand">
                        
                            <img src="https://648d4.https.cdn.softlayer.net/app/static/images/logo-ubidots@2x.png" srcset="https://648d4.https.cdn.softlayer.net/app/static/images/logo-ubidots@2x.png 1x,
                                https://648d4.https.cdn.softlayer.net/app/static/images/logo-ubidots@2x.png 2x,
                                https://648d4.https.cdn.softlayer.net/app/static/images/logo-ubidots@3x.png 3x" alt="Ubidots" title="Ubidots">
                        
                    </a>
                    <div class="text-organizations">
                        

                        
                    </div>
                </div>
            </div>

            
            <div class="col-xs-12 col-sm-12 col-lg-9 menu">
                
                <div class="collapse navbar-collapse" id="menu-collapse">
                    <div class="row">
                        <div class="col-xs-12 col-lg-10 text-center col-fluid nav-container">
                             <!-- END user.is_authenticated -->

                            

                            
                        </div>

                        <div class="col-xs-12 col-lg-2 col-fluid">
                            
                            
                                
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</nav>






    <div class="embed-insight-container ng-scope" ng-controller="embedInsDisplay">
        
        <div ng-show="!outOfCredits &amp;&amp; !trialExpired" class="embed-insight">
        
            
            <embed-insight getins="getInsByCode('u4Bsn_7OU4b4M8Ev-u-ORHZ-Kec')" get-data="embedGetData(insight)" page="true" out-of-credits="outOfCredits" trial-expired="trialExpired" class="ng-isolate-scope"></embed-insight>
            
        </div>

        
        <div ng-show="outOfCredits" style="flex: 1" class="ng-hide">
            <div class="row-fluid col-md-10 col-md-offset-1 col-xs-12 text-center information-box">
                <h1>Account out of credits</h1>
                
                <p>Click <a href="/userdata/plans/">here</a> to purchase more credits.</p>
                
            </div>
        </div>

        <div ng-show="trialExpired" style="flex: 1" class="ng-hide">
            <h1 ng-bind="('business_account_deactivated' | translate)" class="ng-binding">business_account_deactivated</h1>
        </div>
        
    </div>


    <script type="text/javascript" src="https://648d4.https.cdn.softlayer.net/app/static/CACHE/js/cd785acfec6d.js"></script>
    <script src="/wafflejs"></script>


</body></html>
"""
 render contentType: "text/html", data: html, status: 200
}

def whitelist() {
    ["code.highcharts.com",
    "ajax.googleapis.com",
    "app.ubidots.com",
    "ubidots.com",
    "648d4.https.cdn.softlayer.net",
    "fonts.googleapis.com"
    ]
}


// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
    log.debug "URL >> ${url}"
	state.address = url
    state.lastTime = new Date().getTime()
    state.timeSecond = refreshRateMin
    
    timerLoop()
    pollco()
}

def setData(data){
//	log.debug "SetData >> ${data}"
	state._data = data
    
    try{
        data.each{item->
        //	log.debug item
            
            if(item.Type == "Gases - CO2 MH-Z19"){
            	state.carbonDioxide = item.TaskValues[0].Value
                state.temperature = item.TaskValues[1].Value
                log.debug "CO >> ${state.carbonDioxide}"
                log.debug "Temp >> ${state.temperature}"
                sendEvent(name: "carbonDioxide", value: state.carbonDioxide, unit: "ppm" )
                updateMinMaxCo(state.carbonDioxide)
                sendEvent(name: "temperature", value: state.temperature, unit: "C")
                updateMinMaxTemps(state.temperature)
            }
        }
		def nowk = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
        def now = new Date()
        state.lastTime = now.getTime()
        sendEvent(name: "lastCheckin", value: nowk)
        checkNewDay()
    }catch(e){
    	log.error "Error!!! ${e}"
    }
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
/*	def day = new Date().format("EEE", location.timeZone)
	log.debug "resetMinMax day_ ${day}"
	def currentMaxTemp = device.currentValue('maxTemp')
	def currentMinTemp = device.currentValue('minTemp')
	def currentMaxCo = device.currentValue('maxCo')
	def currentMinCo = device.currentValue('minCo')	
	log.debug "resetMinMax day_ ${day}_xT${currentMaxTemp}_nT${currentMinTemp}_xH${currentMaxHumi}_nH${currentMinHumi}"
*/	
	
	def currentTemp = device.currentValue('temperature')
	def currentCo = device.currentValue('carbonDioxide')
    currentTemp = currentTemp ? (int) currentTemp : currentTemp
	log.debug "${device.displayName}: Resetting daily min/max values to current temperature of ${currentTemp}° and humidity of ${currentCo}%"
    sendEvent(name: "maxTemp", value: currentTemp)
    sendEvent(name: "minTemp", value: currentTemp)
    sendEvent(name: "maxCo", value: currentCo)
    sendEvent(name: "minCo", value: currentCo)
    refreshMultiAttributes()
}

def updateMinMaxTemps(temp) {
	def ttime = new Date().format("a hh:mm", location.timeZone)
	if ((temp > device.currentValue('maxTemp')) || (device.currentValue('maxTemp') == null)){
		sendEvent(name: "maxTemp", value: temp)	
		sendEvent(name: "maxTempTime", value: ttime)	
	} else if ((temp < device.currentValue('minTemp')) || (device.currentValue('minTemp') == null)){
		sendEvent(name: "minTemp", value: temp)
		sendEvent(name: "minTempTime", value: ttime)
    }
}

// Check new min or max humidity for the day
def updateMinMaxCo(Co) {
	def ttime = new Date().format("a hh:mm", location.timeZone)
	if ((Co > device.currentValue('maxCo')) || (device.currentValue('maxCo') == null)){
		sendEvent(name: "maxCo", value: Co)
		sendEvent(name: "maxCoTime", value: ttime)	
	} else if ((Co < device.currentValue('minCo')) || (device.currentValue('minCo') == null)){
		sendEvent(name: "minCo", value: Co)
		sendEvent(name: "minCoTime", value: ttime)
    }
}

def timerLoop(){
	getStatusOfESPEasy()    
	startTimer(state.timeSecond.toInteger(), timerLoop)
}

def startTimer(seconds, function) {
    def now = new Date()
	def runTime = new Date(now.getTime() + (seconds * 1000))
	runOnce(runTime, function) // runIn isn't reliable, use runOnce instead
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg, json, status
    try {
        msg = parseLanMessage(hubResponse.description)
        def jsonObj = msg.json
        setData(jsonObj.Sensors)
//	log.debug "SetData >> ${jsonObj.Sensors}"
        
    } catch (e) {
        log.error "Exception caught while parsing data: " + e 
    }
}

def refresh() {
//	getStatusOfESPEasy()
//    pollco()
    initialize()
}

include 'asynchttp_v1'

def initialize() {

/*    
    // call life360, get OAUTH token using password flow, save
    // curl -X POST -H "Authorization: Basic cFJFcXVnYWJSZXRyZTRFc3RldGhlcnVmcmVQdW1hbUV4dWNyRUh1YzptM2ZydXBSZXRSZXN3ZXJFQ2hBUHJFOTZxYWtFZHI0Vg==" 
    //      -F "grant_type=password" -F "username=jeff@hagins.us" -F "password=tondeleo" https://api.life360.com/v3/oauth2/token.json
    

    def url = "https://things.ubidots.com/api/v1.6/variables/5ad8a607c03f975b22f64cf8/values/?token=A1E-SuMotjp8dlJErBDso9BhCACaSDT0ZU"
    
        
    def postBody =  '{"value": 80}'

    def result = null
    
    try {
       
 		httpPost(uri: url, body: postBody) {response -> 
     		result = response
		}
        
    }
    catch (e) {
       log.error "Life360 initializeLife360Connection, error: $e"
       return false;
    }

}*/

try {
    httpPostJson("https://things.ubidots.com/api/v1.6/variables/5ad8a607c03f975b22f64cf8/values/?token=A1E-SuMotjp8dlJErBDso9BhCACaSDT0ZU", '{"value": 70}') { resp ->
        log.debug "response data: ${resp.data}"
        log.debug "response contentType: ${resp.contentType}"
    }
} catch (e) {
    log.debug "something went wrong: $e"
}


/*    def params = [
        uri: 'https://things.ubidots.com',
        path: 'api/v1.6/variables/5ad8a607c03f975b22f64cf8/values/?token=A1E-SuMotjp8dlJErBDso9BhCACaSDT0ZU',
        body: [ '{"value": "80"}'
                
                ],
        contentType: 'application/json',
        
    ]
    asynchttp_v1.post(processResponse, params)*/
}

def processResponse(response, data) { 
	log.debug data
}


def pollco() {
//    def url = "https://api.thingspeak.com/update?key=${apiKey}&field${coField}=${state.carbonDioxide}"
//api/v1.6/devices/{LABEL_DEVICE}/{VARIABLE_LABEL}/values/?token={TOKEN}
	def url = "https://things.ubidots.com/api/v1.6/variables/5ad8a607c03f975b22f64cf8/80/?token=A1E-SuMotjp8dlJErBDso9BhCACaSDT0ZU"
    httpGet(url) { 
        response -> 
        if (response.status != 200 ) {
            log.debug "ThingSpeak logging failed, status = ${response.status}"
        }
    }
    def refreshTime = (updatecycle as int) * 60
    	runIn(refreshTime, pollco)        
        log.debug "Update Temperature to ThingSpeak = ${state.carbonDioxide}C"
}    

def getStatusOfESPEasy() {
    try{
    	def timeGap = new Date().getTime() - Long.valueOf(state.lastTime)
        if(timeGap > 1000 * 60){
            log.warn "ESP Easy device is not connected..."
        }
		log.debug "Try to get data from ${state.address}"
        def options = [
            "method": "GET",
            "path": "/json",
            "headers": [
                "HOST": state.address + ":80",
                "Content-Type": "application/json"
            ]
        ]
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: callback])
        sendHubCommand(myhubAction)
    }catch(e){
    	log.error "Error!!! ${e}"
    }
}