var DHTMLSprite = function(params){
	
	var width = params.width,
	height = params.height, 
	imagesWidth = params.imagesWidth,
	$element = params.$drawTarget.append('<div/>').find(':last'),
	elemStyle = $element[0].style,
	// Store a local reference to the Math.floor function for faster access. 
	mathFloor = Math.floor;
	
	$element.css({
		position: 'relative',
		width: width,
		height: height,
		backgroundImage: 'url(' + params.images + ')'
	});
	
	var that = {
		draw: function (x, y) { 
			elemStyle.left = x + 'px'; 
			elemStyle.top = y + 'px';
		},
		changeImage: function (index) { index *= width;
			var vOffset = -mathFloor(index / imagesWidth) * height;
			var hOffset = -index % imagesWidth;
			elemStyle.backgroundPosition = hOffset + 'px ' + vOffset + 'px';
		},
		show: function () { 
			elemStyle.display = 'block';
		},
		hide: function () {
			elemStyle.display = 'none'; },
			destroy: function () { $element.remove();
		} 
	};
	
	// Return the instance of DHTMLSprite.
	return that;
}
