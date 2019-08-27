function runJobUpdate(jobId) {
    jQuery.get('/ajax/jobs/' + jobId, {}, function(response, textStatus, jqXHR) {
        if ("success" == textStatus) {
            var processedElem = $("#processed");
            processedElem.removeClass('text-info').removeClass('text-danger');
            var processed = response.processed;

            if (processed > 0) {
                processedElem.addClass('text-info');
            } else {
                processedElem.addClass('text-danger');
            }
            processedElem.html(processed);

            if (response.finished) {
                document.location.reload();
                // $("#status").html("<b class='text-success' id='status'>Завершён</b> " + response.finishedText);
                //
                // $("#jobResultContainer").show();
                //
                // if (response.result && response.result.length) {
                //     $("#jobResult").html(response.result.length);
                // }
                //

            }

            if (response.running) {
                setTimeout(function() {
                    runJobUpdate(jobId);
                }, 400);
            }
        } else {
            alert("Ошибка обновления данных: " + textStatus);
            console.log("data is", response);
            console.log("textStatus is", textStatus);
        }
    });
}