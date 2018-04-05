/**
*  KuKuMeter
*
*  Copyright 2017 KuKu
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*/
metadata {
    definition (name: "KuKu Meter Energy Meter2", namespace: "turlvo2", author: "KuKu2") {
        capability "Sensor"
        capability "Power Meter"
        capability "Energy Meter"
        capability "Refresh"
        
        command "energy"
        
    }       
    
    preferences {
        input "pollingInterval", "number", title: "Polling interval (in minutes).", description: "", value:5, displayDuringSetup: false
    }

    tiles(scale:2) {
        valueTile("view", "view", decoration: "flat") {
            state "view", label:'${currentValue} W', icon:'st.Entertainment.entertainment15'
        }
        multiAttributeTile(name:"month", type: "generic", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.energy", key: "PRIMARY_CONTROL") {
                attributeState "energy", label:'이번 달\n${currentValue} kWh',  backgroundColors:[
                                                    [value: 50, 		color: "#153591"],
                                                    [value: 100, 	color: "#1e9cbb"],
                                                    [value: 200, 	color: "#90d2a7"],
                                                    [value: 300, 	color: "#44b621"],
                                                    [value: 400, 	color: "#f1d801"],
                                                    [value: 500, 	color: "#d04e00"], 
                                                    [value: 600, 	color: "#bc2323"]
                                                    ]
            }
            tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
                attributeState("default", label:'Last Update: ${currentValue}', icon: "st.Health & Wellness.health9")
            }
        }
        valueTile("real", "device.power", width: 2, height : 2, decoration: "flat") {
            state "real", label:'${currentValue}\nW'
        }
        valueTile("current", "device.current", width: 2, height : 2, decoration: "flat") {
            state "current", label:'${currentValue}\nA'
        }
        valueTile("voltage", "device.voltage", width: 2, height : 2, decoration: "flat") {
            state "voltage", label:'${currentValue}\nV'
        }
        valueTile("charge", "device.charge", width: 2, height : 2, decoration: "flat") {
            state "charge", label:'${currentValue}\n원'
        }
        
        valueTile("refresh", "device.refresh", width: 2, height : 2, decoration: "flat") {
            state "refresh", label:'REFRESH', action: 'refresh.refresh'
        }

        htmlTile(name:"deepLink", action:"linkApp", whitelist:["code.jquery.com", 
                                                               "ajax.googleapis.com", 
                                                               "fonts.googleapis.com",
                                                               "code.highcharts.com", 
                                                               "enertalk-card.encoredtech.com", 
                                                               "s3-ap-northeast-1.amazonaws.com",
                                                               "s3.amazonaws.com", 
                                                               "ui-hub.encoredtech.com",
                                                               "enertalk-auth.encoredtech.com",
                                                               "api.encoredtech.com",
                                                               "cdnjs.cloudflare.com",
                                                               "encoredtech.com",
                                                               "itunes.apple.com"], width:2, height:2){}

        htmlTile(name:"graphHTML", action: "renderhtml", width: 6, height: 13, whitelist: ["card.enertalk.com", "cdnjs.cloudflare.com", "cdn.rawgit.com"]){}
        main (["view"])
        details (["month", "real", "current", "voltage", "charge", "refresh", "deepLink", "graphHTML"])
    }
}

mappings {
    path("/linkApp") {action: [ GET: "getLinkedApp" ]}
    path("/renderhtml") {action: [ GET: "renderhtml" ]}
}

def renderhtml() {
    def html = """
<!DOCTYPE html>
<html>
	<head lang="en"><meta charset="UTF-8">
		<title>Encored SDK Console</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">

		<!-- Link Swiper's CSS -->
		<link rel="stylesheet" href="../dist/css/swiper.min.css">

		<!-- Demo styles -->
		<style>
		html, body {
			position: relative;
			height: 100 % ;
		}
		body {
			background:  # eee;
			font - family: Helvetica Neue,
			Helvetica,
			Arial,
			sans - serif;
			font - size: 14px;
			color:  # 000;
			margin: 0;
			padding: 0;
		}
		.swiper - container {
			width: 100 % ;
			height: 100 % ;
		}
		.swiper - slide {
			text - align: center;
			font - size: 18px;
			background:  # fff;
			/* Center slide text vertically */
			display: -webkit - box;
			display: -ms - flexbox;
			display: -webkit - flex;
			display: flex;
			-webkit - box - pack: center;
			-ms - flex - pack: center;
			-webkit - justify - content: center;
			justify - content: center;
			-webkit - box - align: center;
			-ms - flex - align: center;
			-webkit - align - items: center;
			align - items: center;
		}
		</style>
	</head>

	<body>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/3.3.1/js/swiper.min.js"></script>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/3.3.1/css/swiper.min.css">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/webcomponentsjs/0.7.21/webcomponents-lite.min.js"></script>
		<script src="https://card.enertalk.com/sdk.js"></script>	

		<div class="swiper-container">
			<div class="swiper-wrapper">						 
				<div class="swiper-slide">
					${card1()}
                    ${card2()}
                    ${card3()}
                    ${card4()}
                    ${card5()}
                    ${card6()}
                    ${card7()}
                    ${card8()}
                    ${card9()}
                    ${card10()}
                    ${card11()}
				</div>
			<!-- Add Pagination -->
			<div class="swiper-pagination"></div>
		</div>
		
		<script>
			var swiper = new Swiper('.swiper-container', {
                                    pagination: '.swiper-pagination',
                                    paginationClickable: true,
                                    loop: true
                                    });
		</script>
	</body>
</html>
                """ 
    render contentType: "text/html", data: html, status: 200

}

def getLinkedApp() {
    def lang = clientLocale?.language
    if ("${lang}" == "ko") {
        lang = "<p style=\'margin-left:15vw; color: #aeaeb0;\'>기기 설정</p>"
    } else {
        lang = "<p style=\'margin-left:5vw; color: #aeaeb0;\'>Setup Device</p>"
    }
    renderHTML() {
        head {
            """
                <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width, height=device-height">
                <style>
                #레이어_1 { margin-left : 17vw; width : 50vw; height : 50vw;}
                .st0{fill:#B5B6BB;}
                </style>
                """ 
        }
        body {
            """
                <div id="container">
                <a id="st-deep-link" href="#">
                <svg version="1.1" id="레이어_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 40 40" style="enable-background:new 0 0 40 40;" xml:space="preserve"><path class="st0" d="M20,0C9,0,0,9,0,20C0,30.5,8,39,18.2,40l3.8-4.8l-3.9-4.8c-4.9-0.9-8.6-5.2-8.6-10.4c0-5.8,4.7-10.5,10.5-10.5
                S30.5,14.2,30.5,20c0,5.1-3.7,9.4-8.5,10.3l3.7,4.5L21.8,40C32,39.1,40,30.5,40,20C40,9,31,0,20,0z"/></svg>
                </a>
                ${lang} 
                </div>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
                <script>
                var ua = navigator.userAgent.toLowerCase();
                if (varUA.match('android') != null) { 
    				//안드로이드 일때 처리
 					\$("#st-deep-link").attr("href", "market://details?id=com.ionicframework.enertalkhome874425");
                } else {
                	\$("#st-deep-link").attr("href", "https://itunes.apple.com/kr/app/enertalk-for-home/id1024660780?mt=8");                
                }
                </script>
                """
        }
    }

}

def installed() {
	log.debug "installed()"
	initialize()
}

def initialize() {
    log.debug "initialize()"

    log.trace("$device.displayName - updated with settings: ${settings.inspect()}")
    device.setDeviceNetworkId(settings.confSiteID.toString())
    // Notify health check about this device with timeout interval equal to 5 failed update requests
    // (add 30 seconds so we don't collide with the 5th request in case that succeeds)
    if (settings.pollingInterval == null || settings.pollingInterval == "" ) settings.pollingInterval = 5
    def healthCheckInterval = settings.pollingInterval.toInteger() * 60 + 30
    sendEvent(name: "checkInterval", value: healthCheckInterval, data: [protocol: "cloud", hubHardwareId: 'xxx'], displayed: false)
    poll()																													
    startPoll()

}


def poll() {
    log.debug "poll()"
    def result = parent.pullData()    
    //log.debug "poll()>> result : $result"
    //results.each { name, value ->
    //    //Parse events and optionally create SmartThings events
    //    log.debug "poll >> name : $name, value : $value"
    //}
}

def refresh() {
    log.debug "refresh()"
    parent.pullData()
}

def ping() {
    log.trace("$device.displayName - checking device health…")
    
    parent.pullData()
}

def updated() {
    log.debug "updated()>> now : ${now()}, updatedLastRanAt: ${state.updatedLastRanAt}"
    if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 4000) {
        state.updatedLastRanAt = now()
        initialize()
    } else {
        log.trace("$device.displayName - updated() ran within the last 2 seconds - skipping")
    }
}

def startPoll() {
    log.debug "startPoll"
    unschedule()
    log.debug "schedule(${settings.pollingInterval}, poll)"
    schedule(settings.pollingInterval.toInteger() * 60 + 30, poll)
}

def card1() {
	return """
					<div id="card-target1"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [
												{
													id: 'ui:h:energywatch:v1',
													params: {
													lang: 'ko'
													}
												}
											],
											accessToken: '${parent.getAccessToken()}',
											target: document.querySelector('#card-target1')
											});										
						</script>
				</div>
                """
}

def card2() {
	return """
				<div class="swiper-slide">
					<div id="card-target2"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:realtime:v3',
													params: {
														lang: 'ko'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target2')
										});
						</script>
				</div>
                """
}

def card3() {
	return """
				<div class="swiper-slide">
					<div id="card-target3"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:alarm:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 1
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target3')
										});
						</script>
				</div>	
                """
}

def card4() {
	return """
				<div class="swiper-slide">
					<div id="card-target4"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:dashboard:v1',
													params: {
													  lang: 'ko',
													  displayUnit: 'watt'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target4')
										});
						</script>
				</div>
                """
}

def card5() {
	return """
				<div class="swiper-slide">
					<div id="card-target5"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:neighborcomparison:v2',
													params: {
														lang: 'ko'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target5')
										});
						</script>
				</div>
                """
}

def card6() {
	return """
				<div class="swiper-slide">

					<div id="card-target6"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:planmain:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 0,
														displayUnit: 'watt',
														disableChangeButton: 1
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target6')
										});
						</script>
				</div>
                """
}

def card7() {
	return """
				<div class="swiper-slide">
					<div id="card-target7"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:realtime:v4',
													params: {
														lang: 'ko'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target7')
										});
						</script>
				</div>
                """
}

def card8() {
	return """
				<div class="swiper-slide">
					<div id="card-target8"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:stepchart:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 0
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target8')
										});
						</script>
				</div>
                """
}

def card9() {
	return """
				<div class="swiper-slide">
					<div id="card-target9"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:thismonth:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 0,
														displayUnit: 'watt'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target9')
										});
						</script>
				</div>
                """
}

def card10() {
	return """
				<div class="swiper-slide">
					<div id="card-target10"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:thismonthchart:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 0,
														displayUnit: 'watt'
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target10')
										});
						</script>
				</div>
                """
}

def card11() {
	return """
				<div class="swiper-slide">
					<div id="card-target11"></div>
						<script>
						var UI = new Encored.UI({
												env: 'production',
												category: 'home',
												iframe: false,
												version: 2
												});	
						UI.renderCard({
										cards: [									  
												{
													id: 'ui:h:usagekeepingbook:v1',
													params: {
														lang: 'ko',
														useDemoLabel: 0,
														displayUnit: 'watt',
														showLayer: 1
													}
												}
										],
										accessToken: '${parent.getAccessToken()}',
										target: document.querySelector('#card-target11')
										});
						</script>
				</div>
			</div>
            """
}