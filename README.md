# cordova-plugin-custom-back-navigation

<!-- https://github.com/user-attachments/assets/cc28fc1a-b149-4466-99d9-ff5f49e7316f -->
![cordova-plugin-custom-back-navigation](https://raw.githubusercontent.com/ollm/cordova-plugin-custom-back-navigation/refs/heads/main/docs/example.gif)

> The `CustomBackNavigation` object offers a set of functions to manage and enhance the new [custom back navigation](https://developer.android.com/guide/navigation/custom-back/support-animations) introduced in Android 14+. It provides a greater control over back navigation behavior, facilitating smoother transitions, improved gesture handling, and better user experience.

**Table of Contents**

- [Installation](#installation)
- [Methods](#methods)
  - [Simple example of plugin usage](#simple-example-of-plugin-usage)
  - [CustomBackNavigation.register](#custombacknavigationregister)
  - [CustomBackNavigation.nextClosesApp](#custombacknavigationnextclosesapp)
  - [CustomBackNavigation.pointerEvents](#custombacknavigationpointerevents)
  - [CustomBackNavigation.useTouchend](#custombacknavigationusetouchend)
  - [CustomBackNavigation.simulateTouchend](#custombacknavigationsimulatetouchend)

## Installation

`cordova plugin add cordova-plugin-custom-back-navigation`

## Methods

This plugin defines global `CustomBackNavigation` object.

Although in the global scope, it is not available until after the `deviceready` event.

```js
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady()
{
    console.log(CustomBackNavigation);
}
```

### Simple example of plugin usage

```js
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady()
{
	CustomBackNavigation.useTouchend();
	CustomBackNavigation.register(true, function(backEvent) {

		switch(backEvent.type)
		{
			case 'started': // Only in Android 14+

				backEvent = {
					type: 'started',
					swipeEdge: 'left', // 'left', 'right' or 'none'
					progress: 0.0,
					touchX: 0.0,
					touchY: 0.0,
					touchInPixelsX: 0.0,
					touchInPixelsY: 0.0,
				}

				CustomBackNavigation.pointerEvents(false);
				CustomBackNavigation.simulateTouchend();
				
				// Started stuff

				break;
			case 'progress': // Only in Android 14+

				backEvent = {
					type: 'progress',
					swipeEdge: 'left', // 'left', 'right' or 'none'
					progress: 0.0,
					touchX: 0.0,
					touchY: 0.0,
					touchInPixelsX: 0.0,
					touchInPixelsY: 0.0,
				}

				// Progress Stuff

				break;
			case 'cancelled': // Only in Android 14+

				backEvent = {
					type: 'cancelled',
				}

				CustomBackNavigation.pointerEvents(true, 50);

				// Cancelled stuff

				break;
			case 'pressed': // Only in Android 13+

				backEvent = {
					type: 'pressed',
				}

				CustomBackNavigation.pointerEvents(true, 300);

				// Pressed stuff

				break;
		}

	});

}
```

### CustomBackNavigation.register

Registers a callback to detect when a `backEvent` is fired.

```js
	CustomBackNavigation.register(Boolean nextClosesApp = true, function(backEvent) {

		switch(backEvent.type)
		{
			case 'started': // Only in Android 14+

				backEvent = {
					type: 'started',
					swipeEdge: 'left', // 'left', 'right' or 'none'
					progress: 0.0,
					touchX: 0.0,
					touchY: 0.0,
					touchInPixelsX: 0.0,
					touchInPixelsY: 0.0,
				}

				break;
			case 'progress': // Only in Android 14+

				backEvent = {
					type: 'progress',
					swipeEdge: 'left', // 'left', 'right' or 'none'
					progress: 0.0,
					touchX: 0.0,
					touchY: 0.0,
					touchInPixelsX: 0.0,
					touchInPixelsY: 0.0,
				}

				break;
			case 'cancelled': // Only in Android 14+

				backEvent = {
					type: 'cancelled',
				}

				break;
			case 'pressed': // Only in Android 13+

				backEvent = {
					type: 'pressed',
				}

				break;
		}

	});
```

- __nextClosesApp__ Sets if the next go back event closes the app or not. This is necessary to make the [back-to-home](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture) animation.

### CustomBackNavigation.nextClosesApp

Sets if the next go back event closes the app or not. This is necessary to make the [back-to-home](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture) animation.

```js
CustomBackNavigation.nextClosesApp(Boolean close = true);
```

- __close__ Sets if the next go back event closes the app or not.

### CustomBackNavigation.pointerEvents

Sometimes, performing the back gesture can accidentally trigger clicks in the WebView. To prevent this, this applies `pointer-events: none` to the `body` tag.

```js
CustomBackNavigation.pointerEvents(Boolean events = true, Int delay = 0);
```

- __events__ Sets if the pointer events are enabled or not.
- __delay__ Sets the delay in milliseconds to enable again the pointer events. This can be used to prevent clicks while an animation/transition is in progress.

### CustomBackNavigation.useTouchend

This is necessary to use `CustomBackNavigation.simulateTouchend`. To simulate a touchend event, data from the previous touchstart event is required. This function registers a `touchstart` event to capture that data.

```js
CustomBackNavigation.useTouchend();
```

### CustomBackNavigation.simulateTouchend

This simulates a `touchend` event. In some cases, a `touchstart` event may be fired before the `backEvent`, but Android/WebView will not send a `touchend` event once the `backEvent` has started. As a result, if you are listening for touch events, it may seem like the finger is still touching the screen.

First you have to use `CustomBackNavigation.useTouchend()`

```js
CustomBackNavigation.simulateTouchend();
```