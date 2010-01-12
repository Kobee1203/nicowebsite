// jsportal (iGoogle-like drag&drop portal v2.0, Mon Sep 14 09:00:00 +0100 2008

// Copyright (c) 2009 Michel Hiemstra (http://michelhiemstra.nl)

// jsportal v2.0 is freely distributable under the terms of an Creative Commons license.
// For details, see the authors web site: http://michelhiemstra/

if(typeof(Prototype) == "undefined")
	throw "Portal requires Prototype to be loaded.";
if(typeof(Effect) == "undefined")
	throw "Portal requires Effects to be loaded.";
if(typeof(Sortable) == "undefined")
	throw "Portal requires Sortable to be loaded.";

var Portal = Class.create({

	initialize : function (options, data) {
		
		// Init an array of Column objects
    this.columnsArray = new Array();
		
		// set options
		this.setOptions(options);
		
		// load data
		this.loadData(data);
		
		// if editor is enabled we proceed
		if (!this.options.editorEnabled || $(this.options.portal)==null) return;
		
		// loop trough columns array
		var columns = new Array();
    for (var index = 0, len = this.columnsArray.length; index < len; index++) {
      var column = this.columnsArray[index];
      var prevColumn = this.columnsArray[index - 1];
      if(index > 0) {
        this.addSplitPane(prevColumn, column);
      }
      
      // Store columns element in order to create the sortable effect
      columns.push(column.element);
    }

    $A(columns).each(function(column) {
			this.initSortable(column, columns);
		}.bind(this));
		
		// get all blocks
		var blocks = $(this.options.portal).select('.'+this.options.block);
		
		// loop trough blocks
		$A(blocks).each(function(block) {
		  this.initBlock(block);
    }.bind(this));
	},
	
	refresh : function () { // Refresh the sortable columns
		// get all available columns
		var columns = $(this.options.portal).select('.'+this.options.column);

		// loop trough columns array
		$A(columns).each(function(column) {
			this.initSortable(column, columns);			
		}.bind(this));
  },
	
	setOptions : function (options) {
		// set options
		this.options = {
			tag				: 'div',
			editorEnabled 	: false,
			portal			: 'portal',
			column			: 'column',
			block			: 'block',
			content			: 'content',
			configElement	: 'config',
			configSave		: 'save-button',
			configCancel	: 'cancel-button',
			configSaved		: 'config-saved',
			handle			: 'draghandle',
			hoverclass		: 'target',
			scroll			: window,
			remove			: 'block-remove',
			config			: 'block-config',
			blocklist		: 'portal-column-block-list',
			saveurl			: false,
			constraint		: false,
			ghosting		: false,
			droponempty		: true
		}
		
		Object.extend(this.options, options || {});
	},
	
	loadData : function (data) {
		// load data for each block
		for (var container in data) {
		  var column = new Column(this, data[container].options);
		  
      // Add items into column
			data[container].children.each(function (block) { 
        column.element.appendChild($(block));
      });
      
      this.columnsArray.push(column);
		}
		/*for (var type in data) {
			data[type].each(function(block) {
				for (var blockname in block) {
					
					switch (type) {
						
						default:
							new Ajax.Updater(blockname + '-content', '/module/'+type+'/data='+block[blockname], { evalScripts : true });
					}
				}
			}.bind(this));
		}*/
	},
	
	initSortable : function (column, columns) {
  	// create sortable
  	Sortable.create(column, {
  		containment : $A(columns),
  		constraint  : this.options.constraint,
  		ghosting	  : this.options.ghosting,
  		tag 		    : this.options.tag,
  		only 		    : this.options.block,
  		dropOnEmpty : this.options.droponempty,
  		handle 		  : this.options.handle,
  		hoverclass 	: this.options.hoverclass,
  		scroll		  : this.options.scroll,
  		onUpdate 	  : function (container) { 			
  			// if we dont have a save url we dont update
  			if (!this.options.saveurl) return;
  			
  			// if we are in the same container we do nothing
  			if (container.id == this.options.blocklist) return;
  			
  			// get blocks in this container
  			var blocks = container.getElementsByClassName(this.options.block);
  			
  			// serialize all blocks in this container
  			var postBody = container.id + ':';
  			postBody += $A(blocks).pluck('id').join(',');
  			postBody = 'value=' + escape(postBody);
  			
  			// save it to the database
  			new Ajax.Request(this.options.saveurl, { method: 'post', postBody: postBody, onComplete : function (t) {
  				$('data').update(t.responseText + $('data').innerHTML);
  			} });
  								
  		}.bind(this)
  	});
  	
  	column.undoPositioned(); // bug fix with split pane
  },
  
  addSplitPane : function(prevColumn, column) {
    var splitPane = new SplitPane(prevColumn.element.identify(), prevColumn.getRealWidth(), column.element.identify(), 0, column.getRealWidth(), { 
  			active:  true, 
  			div1MinWidth: parseFloat(prevColumn.options.style.minWidth), 
  			div2MinWidth: parseFloat(column.options.style.minWidth),
  			dividerHoverColor: "#00FF00"
  		}
  	);
  	column.setSplitPaneLeft(splitPane);
  	prevColumn.setSplitPaneRight(splitPane);
  },
  
  removeSplitPaneFromColumn : function(column) {
    var index = this.columnsArray.indexOf(column);

    if(column.splitPaneLeft) {
      var leftElement = column.splitPaneLeft.div1;
      column.splitPaneLeft.divider.remove();
      SplitPane.cache.removeByElement(column.splitPaneLeft);
      SplitPane.cacheIndex = SplitPane.cacheIndex - 1;
      
      column.splitPaneLeft = null;
      this.columnsArray[index - 1].splitPaneRight = null;
    }
    if(column.splitPaneRight) {
      var rightElement = column.splitPaneRight.div2;
      column.splitPaneRight.divider.remove();
      SplitPane.cache.removeByElement(column.splitPaneRight);
      SplitPane.cacheIndex = SplitPane.cacheIndex - 1;
      
      column.splitPaneRight = null;
      this.columnsArray[index + 1].splitPaneLeft = null;
    }
    
    // Add new splitpane if necessary 
    if(leftElement && rightElement) {
      this.addSplitPane(this.columnsArray[index - 1], this.columnsArray[index + 1]);
      SplitPane.cache[SplitPane.cacheIndex - 1].set();
    }
  },

	addNewColumn : function () {  
    if(this.columnsArray.length == 0) {
      var options = {
		    style    : {
          width : '100%'
        }
      };
      var column = new Column(this, options);
      var remainWidth = 0;
      var modifiedColumns = new HashMap();
    } else {
      // Create and add the new column to the portal
      var column = new Column(this, options);
      var remainWidth = column.getRealMinWidth();
      var modifiedColumns = new HashMap();
      for(var index = this.columnsArray.length; index > 0; index--) {
        var col = this.columnsArray[index - 1];
        var width = col.getRealWidth();
        var minWidth = col.getMinWidth();
        if((width - remainWidth) > minWidth) {
          modifiedColumns.put(col, (width - remainWidth));
          remainWidth = 0;
          break;
        } else {
          var diff = width - minWidth;
          modifiedColumns.put(col, minWidth);
          remainWidth = remainWidth - diff;
        }
      }
    }
    
    if(remainWidth == 0) {
      modifiedColumns.each(function(column, newWidth) {
        column.element.setStyle({width: newWidth + '%'});
      });
    
      // Store the column object
      this.columnsArray.push(column);
      
      // Check if there are several columns in order to add a divider and update others
      if(this.columnsArray.length > 1) {
        // add new splitpane with this new column
        var prevColumn = this.columnsArray[this.columnsArray.length - 2];
        this.addSplitPane(prevColumn, column);
        SplitPane.cache[SplitPane.cacheIndex - 1].set();
        // Update all dividers location
        SplitPane.updateAllDividers();
      }
      
      // Set the new 'wrapper' width
      //$('wrapper').setStyle({width: ($('wrapper').getWidth()+column.element.getWidth())+'px'});
      
      // Highlight the new column
      new Effect.Highlight(column.element);
    } else {
      column.element.remove();
      alert('There is not space to add an other column');
    }
  },
	
	addNewBlock : function (column, block) {
    if(!block) {
      var newBlock = new Element('div');
      newBlock.identify();
      newBlock.addClassName(this.options.block);
      newBlock.update($('block').innerHTML);
    } else {
      var newBlock = block;
    }
    this.columnsArray[0].element.appendChild(newBlock);
    this.initBlock(newBlock);
  },
  
  initBlock : function (block) {
    // enable controls if available
    var blockCtrlElt = block.select('.block-controls');
  	if (blockCtrlElt!=null && typeof(blockCtrlElt) == 'object') {
  		blockCtrlElt.invoke('setStyle', {display: 'block'});
  	}
  	
  	// detail, set cursor style to move when in admin modus
  	var handleElt = block.select('.'+this.options.handle);
  	if (handleElt!=null && typeof(handleElt) == 'object') {
  		handleElt.invoke('setStyle', {cursor : 'move'});
  	}
  
  	// observe delete block button
  	var removeElt = block.select('.'+this.options.remove);
    if (removeElt!=null && typeof(removeElt) == 'object') {
      removeElt.invoke('observe', 'click', function (e) {
  			if (confirm('Are you sure you wish to delete portal block?')) {
  				new Ajax.Request(this.options.saveurl + '/delete/', { method: 'post', postBody: 'block='+block.id });
          $(block.id).hide();
  			}
  		}.bind(this));
  	}
  }
});


var Column = Class.create({
  initialize: function(portal, options) {
    // Set portal object
    this.portal = portal;
    
    // set options
		this.setOptions(options);
		
		// Create the element
		this.makeElement();
		
		if(this.portal) {
  		// Add the element to the 'portal' parent
  		$(this.portal.options.portal).appendChild(this.element);
  		  
      // Recalculate column width in percentage in removing pixels in more (borders, paddings, margins)
  		this.calculateRealWidth();
		}
  },
  
  setOptions : function (options) {  
		// set options
		this.options = {
		  style    : {
        display  : 'block',
        float    : 'left',
        minWidth : '100px'
      },
		  template : $('column')
		}

		if(options) {
      var style = Object.extend(this.options.style, options.style || {});
      Object.extend(this.options, options || {});
      this.options.style = style;
    }
	},
	
	portalElement : function() {
      return $(this.portal.options.portal);
  },
	
	makeElement : function() {
    // Make column element, id, classname, style, content
    this.element = this.options.template.clone(true);
    this.element.setStyle(this.options.style);
    this.element.writeAttribute('id', null); // remove id
    this.element.identify(); // Set the new unique ID
    // Add events
    this.addEvents();
  },
  
  getRealWidth : function() {
    if(!this.realWidth) {
      this.calculateRealWidth();
    }
    
    return parseFloat(this.element.getStyle('width'));
  },
  
  getMinWidth : function() {
    if(!this.minWidth) {
      this.calculateRealWidth();
    }
    
    // The minimum with in percentage
    return this.minWidth;
  },
  
  getRealMinWidth : function() {
    if(!this.realMinWidth) {
      this.calculateRealWidth();
    }
    
    // The real minimum with in percentage
    return this.realMinWidth;
  },
  
  // Calculate column width in percentage in removing pixels in more (borders, paddings, margins)
  // Call this method after having added the column to the parent
  calculateRealWidth : function() {
		  
		  var pixelsInMorePercent = this.pixelsInMorePercent();
		  
		  this.minWidth = parseFloat(this.options.style.minWidth) * 100.0 / this.element.parentNode.getWidth();
		  this.minWidth = this.minWidth.roundNumber(4);
		  
      // Calculate the new column width in percentage
		  var sWidth = this.element.getStyle('width');
      if(sWidth.endsWith('%')) {
		    var width = parseFloat(sWidth);
      } else {
        var width = (parseFloat(sWidth) * 100.0) / this.element.parentNode.getWidth();
        width = width.roundNumber(4) + pixelsInMorePercent;
      }
      
      // Check if the width is not defined
      if(width <=0) {
        // Calculate the new column width in percentage from the minimum width
        var width = this.minWidth + pixelsInMorePercent;
        this.realWidth = this.minWidth;
        this.realMinWidth = width;
      }
      else {
        // Set the real width in percentage without pixels in more      
        this.realWidth = width - pixelsInMorePercent;
        this.realMinWidth = this.minWidth + pixelsInMorePercent;
      }
      
      // Set the column width in percentage
      this.element.setStyle({width : this.realWidth+'%'});
      //this.element.setStyle({minWidth : this.realMinWidth+'%'});
  },
  
  pixelsInMorePercent : function() {
    // Retrieve the pixels in more
    var pixelsInMore = 0;
    var marginLeft = parseFloat(this.element.getStyle('margin-left'));
    pixelsInMore += marginLeft || 0;
	  var marginRight = parseFloat(this.element.getStyle('margin-right'));
	  pixelsInMore += marginRight || 0;
	  var paddingLeft = parseFloat(this.element.getStyle('padding-left'));
	  pixelsInMore += paddingLeft || 0;
	  var paddingRight = parseFloat(this.element.getStyle('padding-right'));
	  pixelsInMore += paddingRight || 0;
    var borderLeftWidth = parseFloat(this.element.getStyle('border-left-width'));
    pixelsInMore += borderLeftWidth || 0;
	  var borderRightWidth = parseFloat(this.element.getStyle('border-right-width'));
	  pixelsInMore += borderRightWidth || 0;
    
    // Calculate the pixels in more in percentage according to the 'portal' parent
    var pixelsInMorePercent = (pixelsInMore * 100.0) / this.element.parentNode.getWidth();
    
    return pixelsInMorePercent.roundNumber(4);
  },
  
  setSplitPaneLeft : function(splitPaneLeft) {
    this.splitPaneLeft = splitPaneLeft;
  },
  
  setSplitPaneRight : function(splitPaneRight) {
    this.splitPaneRight = splitPaneRight;
  },
  
  addEvents : function() {
    // observe display the control buttons
    Event.observe(this.element, 'mouseover', function (e) {
      var columnCtrlElt = this.element.select('.column-controls');
      if (columnCtrlElt != null && typeof(columnCtrlElt) == 'object') {
        columnCtrlElt.invoke('setStyle', {display: 'block'});
      }
    }.bind(this));
    
    // observe don't display the control buttons
    Event.observe(this.element, 'mouseout', function (e) {
      var columnCtrlElt = this.element.select('.column-controls');
      if (columnCtrlElt != null && typeof(columnCtrlElt) == 'object') {
        columnCtrlElt.invoke('setStyle', {display: 'none'});
      }
    }.bind(this));
  
  	// observe delete column button
  	var removeElt = this.element.select('.column-remove');
    if (removeElt != null && typeof(removeElt) == 'object') {
      removeElt.invoke('observe', 'click', function (e) {
        if (confirm('Are you sure you wish to delete portal column?')) {
          //$('wrapper').setStyle({width: ($('wrapper').getWidth() - column.getWidth())+'px'});
          var id = this.element.id;
          var w = this.getRealWidth();

          // remove splitpanes (left and/or right) and create new splitpane if necessary
          this.portal.removeSplitPaneFromColumn(this);
          
          // Remove element and column object
          this.element.remove();//.hide();
          portal.columnsArray.removeByElement(this);
          
          // Update the last column width
          if(this.portal.columnsArray.length > 0) {
            var lastColumn = this.portal.columnsArray[this.portal.columnsArray.length - 1];
            var newWidth = lastColumn.getRealWidth() + lastColumn.pixelsInMorePercent() + w;
            lastColumn.element.setStyle({width: newWidth + '%'});
          }
          
          // Update all dividers location
          SplitPane.updateAllDividers();
          
          new Ajax.Request(this.portal.options.saveurl + '/delete/', { method: 'post', postBody: 'column='+id });
        }
  		}.bind(this));
  	}
  }
});

Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};
Array.prototype.removeByElement = function(arrayElement) {
  for(var i=0; i<this.length; i++) { 
    if(this[i] == arrayElement) {
      this.splice(i,1); 
    }
  } 
}

Number.prototype.roundNumber = function(exp) {
  var virgule = Math.round(Math.pow(10, exp));
  return Math.round(this*virgule)/virgule;
};
test = 21.6546;
testarrondi=test.roundNumber(3);
//console.log(testarrondi);