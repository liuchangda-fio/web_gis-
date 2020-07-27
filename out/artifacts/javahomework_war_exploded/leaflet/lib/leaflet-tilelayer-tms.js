L.TileLayer.TMS = L.TileLayer.extend({
	getTileUrl: function (coords) {
		var data = {
			r: L.Browser.retina ? '@2x' : '',
			s: this._getSubdomain(coords),
			x: coords.x,
			y: Math.pow(2,coords.z) - 1 - coords.y,
			z: this._getZoomForUrl()
		};
		if (this._map && !this._map.options.crs.infinite) {
			var invertedY = this._globalTileRange.max.y - coords.y;
			if (this.options.tms) {
				data['y'] = invertedY;
			}
			data['-y'] = invertedY;
		}

		return L.Util.template(this._url, L.Util.extend(data, this.options));
	},
});

L.tileLayer.tms = function (url, options) {
    return new L.TileLayer.TMS(url, options);
};
