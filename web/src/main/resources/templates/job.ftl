<#setting date_format="dd.MM.yyyy">
<#import "util.ftl" as util />
<#import "/spring.ftl" as spring/>

<@util.page>

    <#if job?has_content>
        <div style="width: 800px">
            <div class="card card-body">
                <table class="table table-sm">
                    <tr>
                        <td>Директория: </td>
                        <td>
                            <b>${job.scanRoot!''}</b>
                        </td>
                    </tr>

                    <tr>
                        <td>Тип поиска: </td>
                        <td>
                            <b>${job.scanTypeName!''}</b>
                            <#if job.fileExtensions?has_content>
                                <br />
                                <small>Имеющих расширения: <b>${job.fileExtensions?join(", ")}</b></small>
                            </#if>
                        </td>
                    </tr>

                    <tr>
                        <td>Статус: </td>
                        <td id="status">
                            <#if job.running?? && job.running>
                                <b class="text-info">Выполняется</b>
                                <buton type="button" class="btn btn-secondary btn-sm" onclick="document.location.reload();" id="reloadButton">
                                    Обновить
                                </buton>
                            <#elseif job.finished?has_content>
                                <b class="text-success">Завершён</b> ${job.finished?string("dd.MM.yyyy HH:mm")}
                            <#else>
                                <b class="text-muted" >Создан</b>
                            </#if>
                        </td>
                    </tr>

                    <tr>
                        <td>Обработано файлов: </td>
                        <td>
                            <#if (job.processed>0) >
                                <b class="text-info" id="processed">${job.processed}</b>
                            <#else>
                                <b class="text-danger" id="processed">${job.processed}</b>
                            </#if>
                        </td>
                    </tr>

                    <tr>
                        <td>Создан: </td>
                        <td>
                            <b>${job.created?string("dd.MM.yyyy HH:mm")}</b>
                        </td>
                    </tr>

                    <tr <#if !job.finished?has_content>style="display:none"</#if> id="jobResultContainer" >
                            <td>Найдено дубликатов:</td>
                            <td>
                                <b id="jobResult">${(job.result?size)!0}</b>
                            </td>
                    </tr>

                </table>
            </div>
        </div>

        <br />

        <#if !job.result?has_content>
            <div class="card card-body" style="width:100%">
                <b class="text-warning">Необходимо дождаться выполнения поиска <img src="<@spring.url '/img/loader.gif'/>" width="64" height="64"/></b>
            </div>

            <br />
        </#if>


        <div class="card card-body">

                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>Файл</td>
                        <td>Дубликаты</td>
                    </tr>
                    </thead>
                        <tbody>
                        <#if job.finished?has_content>
                            <#if job.result?has_content>
                                <#list job.result?sort_by("name") as dupFile>
                                    <tr>
                                        <td>
                                            <small>${dupFile?index + 1}</small>
                                        </td>
                                        <td>
                                            <small>
                                                <b>${dupFile.name!''}</b>
                                                (${dupFile.sizeName!''})
                                            </small>
                                        </td>
                                        <td>
                                            <#if dupFile.locations?has_content>
                                                <table class="table">
                                                    <#list dupFile.locations as location>
                                                        <form action="/jobs/${job.id!''}" method="post">
                                                            <input type="hidden" name="location" value = "${location!''}" />
                                                            <tr>
                                                                <td>
                                                                    <small>${location!''}</small>
                                                                </td>
                                                                <td align="right">
                                                                    <small>
                                                                        <button type="submit" onclick="return confirm('Вы уверены?')">Удалить</button>
                                                                    </small>
                                                                </td>
                                                            </tr>
                                                        </form>
                                                    </#list>
                                                </table>
                                            </#if>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                        </#if>
                        </tbody>
                </table>
        </div>

        <#if job.running?? && job.running>
            <script>
                runJobUpdate('${job.id}');
            </script>
        </#if>

    <#else>
        <p class="text-info">
            Ничего не найдено
        </p>
    </#if>

</@util.page>