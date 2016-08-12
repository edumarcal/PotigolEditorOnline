// Author: Diego Perini <dperini@nwbox.com>
var sb = new Array(
	'leftbox', 'rightbox', 'scrollLeft', 'scrollRight', 
	'caretPos', 'maxLength', 'textLength', 'availLength',
	'beforeCaret', 'afterCaret', 'selectedText'
)

for (var i in sb) eval('var ' + sb[i] + ' = {}')

var os = 0
var oe = 0
function update(o) {
	var t = o.value, s = getSelectionStart(o), e = getSelectionEnd(o)
	if (s == os && e == oe) return
	caretPos.firstChild.nodeValue = s
	maxLength.firstChild.nodeValue = o.getAttribute('maxLength')
	textLength.firstChild.nodeValue = t.length
	availLength.firstChild.nodeValue = o.getAttribute('maxLength') - t.length
	afterCaret.firstChild.nodeValue = t.substring(s).replace(/ /g, '\xa0') || '\xa0'
	beforeCaret.firstChild.nodeValue = t.substring(0, s).replace(/ /g, '\xa0') || '\xa0'
	selectedText.firstChild.nodeValue = t.substring(s, e).replace(/ /g, '\xa0') || '\xa0'
	rightbox.value = scrollRight.firstChild.nodeValue = t.substring(s).replace(/ /g, '\xa0') || '\xa0'
	leftbox.value = scrollLeft.firstChild.nodeValue = t.substring(0, s).replace(/ /g, '\xa0') || '\xa0'
	os = s
	oe = e
	return true
}

function setup() {
	for (var i in sb) eval(sb[i] + ' = document.getElementById(sb[i])')
	update(document.getElementById('textbox'))
}

function getSelectionStart(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate()
		r.moveEnd('character', o.value.length)
		if (r.text == '') return o.value.length
		return o.value.lastIndexOf(r.text)
	} else return o.selectionStart
}

function getSelectionEnd(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate()
		r.moveStart('character', -o.value.length)
		return r.text.length
	} else return o.selectionEnd
}