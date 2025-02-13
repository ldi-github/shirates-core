// Shirates (shirates-core)

$(function () {

    // Get row by screenshot file name
    function getRowByScreenshotName(screenshotFileName) {
        var $trs = $("#log-lines tr[data-last-screenshot='" + screenshotFileName + "']")
        if ($trs.length === 0) {
            return null
        }
        return $trs[0]
    }

    // Highlight row
    function highlightRow(tr) {
        var $currentRow = $(tr)

        var $oldRow = $(tr).siblings(".text-highlighted")
        $oldRow.children("td").removeClass("text-highlighted")
        $oldRow.removeClass("text-highlighted")

        $currentRow.addClass("text-highlighted")
        $currentRow.children("td").addClass("text-highlighted")
        $currentRow.children("td.result").removeClass("text-highlighted")
    }

    // Highlight image title
    function highlightImageTitle(fileName) {
        $(".image-gallery .image-title-text").removeClass("text-highlighted")
        if (fileName === "") {
            return
        }

        var $titleText = $(".image-title-text").filter(function (index, element) {
            return element.textContent.trim() === fileName
        })
        if ($titleText.length === 0) {
            return
        }

        $titleText.addClass("text-highlighted")

        var imageFrame = $titleText.parent().parent()[0]
        return imageFrame
    }

    // Get highlighted row
    function getHighlightedRow() {
        var $row = $("#log-lines tr.text-highlighted")
        if ($row.length !== 0) {
            return $row[0]
        }
    }

    // moveToNextOrPreviousRow
    function moveToNextOrPreviousRow(next) {

        var row = getHighlightedRow()
        if (!row) {
            return false
        }
        var seq = parseInt($(row).attr("data-seq"))
        if (next) {
            seq += 1
        } else {
            seq -= 1
        }
        if (seq < 1) {
            return false
        }
        var $tr = $(`#log-lines tr[data-seq=${seq}]`)
        if ($tr.length === 0) {
            return false
        }
        var tr = $tr[0]
        highlightRowAndImage(tr)
        tr.scrollIntoView({behavior: "smooth", block: "center", inline: "nearest"})

        return true
    }

    // Get highlighted fileName
    function getHighlightedFileName() {
        var highlighted = $(".image-gallery span.text-highlighted")
        if (highlighted.length === 0) {
            return ""
        }
        return highlighted[0].textContent
    }

    // isModalOpen
    function isModalOpen() {
        var $modalPanel = $("#modal-panel")
        return $modalPanel.is(":visible")
    }


    // Mouse wheel on image gallery
    window.onmousewheel = function (event) {
        var gallery = $(".image-gallery")[0]
        var galleryRect = gallery.getBoundingClientRect()
        if (event.y < galleryRect.y) {
            return
        }

        var deltaX = event.wheelDelta
        var ua = window.navigator.userAgent.toLowerCase()
        if (ua.indexOf("mac os") !== -1) {
            deltaX = deltaX / 10
        }
        gallery.scrollLeft -= deltaX
        console.log("scrollLeft=" + gallery.scrollLeft + ", deltaX=" + deltaX)
    }

    // On image-frame click
    $(".image-frame").click(function (e) {
        var imageFrame = this
        onImageFrameClick(imageFrame, e)
    })

    function onImageFrameClick(imageFrame, e) {
        var fileName = $(imageFrame).find(".image-title-text").text()
        if (fileName === "") {
            return
        }

        var tr = getRowByScreenshotName(fileName)
        var imageFrame = highlightImageTitle(fileName)
        highlightRow(tr)
        scrollToRowAndImage(tr, imageFrame)

        if (!!e && e.shiftKey === true) {
            var highlightedFileName = getHighlightedFileName()
            if (highlightedFileName !== "") {
                openPictureXml(highlightedFileName)
                return
            }
        }
    }

    // On image-frame dblclick
    $(".image-frame").dblclick(function () {
        var fileName = $(this).find(".image-title-text").text()
        openModal(fileName)
        return false
    })

    // Open modal
    function openModal(fileName) {
        if (!!fileName === false) {
            fileName = $(getThisFrame()).find("img").attr("src")
        }
        if (fileName.endsWith(".png") === false) {
            return
        }
        var $modalPanel = $("#modal-panel")
        if ($modalPanel.is(":hidden")) {
            $modalPanel.fadeIn(200)
        }

        var src = fileName.replace("#", "%23")
        var tag = "<img id='modal-image' src='" + src + "'>"
        $("#modal-image").remove()
        $("#modal-image-container").append(tag)
    }

    // On modal-panel click
    $("#modal-panel").on("click contextmenu", function (e) {
        var $image = $("#modal-image")
        var imageLeft = $image.offset().left
        var imageTop = $image.offset().top
        var imageRight = imageLeft + $image.outerWidth()
        var imageBottom = imageTop + $image.outerHeight()
        if (e.clientX < imageLeft || e.clientX > imageRight || e.clientY < imageTop || e.clientY > imageBottom) {
            // out of image
            $(this).fadeToggle()
        } else {
            // on image
            if (e.which === 1) {
                // left click
                if (e.shiftKey === true) {
                    var highlightedFileName = getHighlightedFileName()
                    if (highlightedFileName !== "") {
                        openPictureXml(highlightedFileName)
                        return false
                    }
                }
                moveToNextOrPreviousImage(true)
                openModal()
                return false
            } else if (e.which === 3) {
                // right click
                moveToNextOrPreviousImage(false)
                openModal()
                return false
            }
        }
    })

    // keyDown
    $(window).keydown(function (e) {
        if (e.keyCode === 27) {
            // Esc
            var $modalPanel = $("#modal-panel")
            if ($modalPanel.is(":visible")) {
                $modalPanel.fadeOut()
            }
            return
        }

        if (isModalOpen()) {
            if (e.keyCode === 37 || e.keyCode === 38) {
                // left or up
                moveToNextOrPreviousImage(false)
                openModal()
                return
            }
            if (e.keyCode === 39 || e.keyCode === 40) {
                // right or down
                moveToNextOrPreviousImage(true)
                openModal()
                return
            }
            if (e.keyCode === 32) {
                return false
            }
        }

        if (e.keyCode === 38) {
            // up
            if (moveToNextOrPreviousRow(false)) {
                return false
            }
        } else if (e.keyCode === 40) {
            // down
            if (moveToNextOrPreviousRow(true)) {
                return false
            }
        } else if (e.keyCode === 37) {
            // left
            moveToNextOrPreviousImage(false)
            return false
        } else if (e.keyCode === 39) {
            // right
            moveToNextOrPreviousImage(true)
            return false
        } else if (e.keyCode === 32) {
            // space
            var tr = getHighlightedRow()
            if (!!tr) {
                var fileName = $(tr).attr("data-last-screenshot")
                openModal(fileName)
                return false
            }
        }
    })

    function getThisFrame() {
        var $thisFrame = $(".image-gallery span.text-highlighted").parent().parent()
        if ($thisFrame.length === 0) {
            return null
        }
        return $thisFrame[0]
    }

    // moveToNextOrPreviousImage
    function moveToNextOrPreviousImage(next) {
        var thisFrame = getThisFrame()

        var $targetFrame
        if (thisFrame == null) {
            $targetFrame = $(".image-frame")
        } else if (next) {
            $targetFrame = $(thisFrame).next(".image-frame")
        } else {
            $targetFrame = $(thisFrame).prev(".image-frame")
        }
        if ($targetFrame.length === 0) {
            return
        }

        onImageFrameClick($targetFrame[0])
    }

    // On tr in scenario-lines/case-lines dblclick
    function setNavigationToLogLine(filter) {
        $(filter).dblclick(function () {
            var lineNo = $(this).attr("data-line")
            var targetTr = $("#log-lines tr[data-line='" + lineNo + "']")[0]
            targetTr.scrollIntoView({behavior: "smooth", block: "center"});
            setTimeout(function () {
                highlightRowAndImage(targetTr)
            })
        })
    }

    setNavigationToLogLine("#irregular-lines tr")
    setNavigationToLogLine("#scenario-lines tr")
    setNavigationToLogLine("#case-lines tr")

    // openPictureXml
    function openPictureXml(fileName) {
        if (fileName.endsWith(".png")) {
            if ($(".test-mode").attr("data-test-mode") === "Vision") {
                fileName = fileName.substring(0, fileName.length - 4) + "_recognizeText.json"
                window.open(fileName)
            } else {
                fileName = fileName.substring(0, fileName.length - 4) + ".xml"
                window.open(fileName)
            }
        }
    }

    // On tr in log-lines click
    $("#log-lines tr").click(function (e) {
        var tr = this
        highlightRowAndImage(tr)
        if (e.metaKey === true || e.ctrlKey === true || e.shiftKey === true) {
            var fileName = $(tr).attr("data-last-screenshot")
            openPictureXml(fileName)
        }
    })

    // highlightRowAndImage
    function highlightRowAndImage(tr) {
        highlightRow(tr)

        var fileName = $(tr).attr("data-last-screenshot")
        var imageFrame
        if (fileName !== "") {
            imageFrame = highlightImageTitle(fileName)
        }

        scrollToRowAndImage(tr, imageFrame)
    }

    var lastRowScrollTime = (new Date()).getTime()

    // scrollToRowAndImage
    function scrollToRowAndImage(tr, imageFrame) {

        var behavior = "smooth"
        var time = (new Date()).getTime()
        var duration = time - lastRowScrollTime
        if (duration < 400) {
            behavior = "auto"
        }
        lastRowScrollTime = time

        setTimeout(function () {
            tr.scrollIntoView({behavior: behavior, block: "center", inline: "nearest"})
            if (!!imageFrame) {
                setTimeout(function () {
                    setTimeout(function () {
                        if (imageFrame != null) {
                            imageFrame.scrollIntoView({behavior: "smooth", inline: "center"})
                        }
                    })
                }, 800)
            }
        })
    }

    // On tr in log-lines dblclick
    $("#log-lines tr").dblclick(function () {
        var tr = this
        var fileName = $(tr).attr("data-last-screenshot")
        openModal(fileName)
    })

    // On resize window
    function onResizeWindow() {
        var bodyHeight = $("body").height()
        var separatorHeight = $("#separator").height()
        var imageGalleryHeight = $(".image-gallery").height()
        var mainHeight = bodyHeight - (separatorHeight + imageGalleryHeight)
        $(".main").css("height", mainHeight + "px")
    }

    // On Separator moved
    function onSeparatorMoved(separatorY) {
        var bodyHeight = $("body").height()
        var separatorHeight = $("#separator").height()
        var imageGalleryHeight = bodyHeight - (separatorY + separatorHeight)
        $(".image-gallery").css("height", imageGalleryHeight + "px")
    }

    // Drag separator
    $("#separator").mousedown(function () {
        $(this).addClass("separator-dragging")
    })
    $("body").mouseup(function () {
        $("#separator").removeClass("separator-dragging")
    })
    $("body").mousemove(function (e) {
        var separator = $("#separator")[0]
        if ($(separator).hasClass("separator-dragging") === false) {
            return
        }
        var y = e.clientY
        var clientHeight = document.body.clientHeight
        var p = y / clientHeight
        if (p < 0.2) {
            y = clientHeight * 0.2
        }
        if (p > 0.8) {
            y = clientHeight * 0.8
        }

        $(separator).offset({top: y})
        $(".main").height(y)

        onSeparatorMoved(y)
    })

    // Initialize styles
    var bodyHeight = $("body").height()
    var separatorHeight = $("#separator").height()
    var imageGalleryHeight = bodyHeight * 0.4 - separatorHeight
    var mainHeight = bodyHeight - (separatorHeight + imageGalleryHeight)
    $(".image-gallery").css("height", imageGalleryHeight + "px")
    $(".main").css("height", mainHeight + "px")
    $(window).resize(function () {
        onResizeWindow()
    })

})