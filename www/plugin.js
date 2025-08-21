var exec = require('cordova/exec'),
	cordova = require('cordova');

var PLUGIN_NAME = 'CustomBackNavigation',
	registred = false,
	callback = false;

function register(nextClosesApp = true, _callback = false) 
{
	callback = _callback;

	if(!registred)
	{
		exec(parse, null, PLUGIN_NAME, 'register', [!nextClosesApp]);
		registred = true;
	}
}

function parse(backEvent)
{
	if(backEvent.touchX !== undefined)
	{
		const devicePixelRatio = window.devicePixelRatio || 1;

		backEvent.touchInPixelsX = backEvent.touchX;
		backEvent.touchX = backEvent.touchX / devicePixelRatio;

		backEvent.touchInPixelsY = backEvent.touchY;
		backEvent.touchY = backEvent.touchY / devicePixelRatio;
	}

	if(backEvent.swipeEdge !== undefined)
	{
		if(!backEvent.swipeEdge)
			backEvent.swipeEdge = 'left';
		else if(backEvent.swipeEdge == 1)
			backEvent.swipeEdge = 'right';
		else //  if(backEvent.swipeEdge == 2)
			backEvent.swipeEdge = 'none';
	}

	if(callback)
		callback(backEvent);
}

function nextClosesApp(close = true)
{
	exec(null, null, PLUGIN_NAME, 'setEnabled', [!close]);
}

var pointerEventsST = false;

function pointerEvents(events = false, delay = 0)
{
	if(events)
	{
		if(delay)
		{
			pointerEventsST = setTimeout(function(){

				document.body.style.pointerEvents = '';

			}, delay);
		}
		else
		{
			document.body.style.pointerEvents = '';
		}
	}
	else
	{
		if(pointerEventsST !== false)
			clearTimeout(pointerEventsST);

		document.body.style.pointerEvents = 'none';
	}
}

var usingTouchend = false,
	lastTouch = false;

function useTouchend()
{
	if(!usingTouchend)
	{
		usingTouchend = true;

		window.addEventListener('touchstart', function(event) {

			lastTouch = event;

		});
	}
}

function simulateTouchend()
{
	if(lastTouch)
	{
		const touchendEvent = new TouchEvent('touchend', {
			bubbles: true,
			cancelable: true,
			touches: [], // No active touches, since it's an end event
			targetTouches: lastTouch.targetTouches,
			changedTouches: lastTouch.changedTouches,
		});

		lastTouch.target.dispatchEvent(touchendEvent);
	}
	else if(!usingTouchend)
	{
		throw new Error('You must have executed useTouchend before');
	}
}

var CustomBackNavigation = {
	register: register,
	nextClosesApp: nextClosesApp,
	pointerEvents: pointerEvents,
	useTouchend: useTouchend,
	simulateTouchend: simulateTouchend,
};

module.exports = CustomBackNavigation;