<#setting date_format="dd.MM.yyyy">
<#import "util.ftl" as util />
<#import "/spring.ftl" as spring/>

<@util.page>

    <#if jobs?has_content>
        <table class="table table-sm table-striped">
            <thead>
                <tr>
                    <th>
                        #
                    </th>
                    <th>
                        Директория
                    </th>
                    <th>
                        Тип поиска
                    </th>
                    <th>
                        Статус
                    </th>
                    <th>
                        Дата создания
                    </th>
                </tr>
            </thead>
            <tbody>
                <#list jobs as job>
                    <tr>
                        <td>
                            <a href="/jobs/${job.id!''}" title="Перейти к результатам поиска">${job?index + 1}.</a>
                        </td>
                        <td>
                            <a href="/jobs/${job.id!''}" title="Перейти к результатам поиска">${job.scanRoot!''}</a>
                        </td>
                        <td>
                            ${job.scanTypeName!''}
                            <#if job.fileExtensions?has_content>
                                <br />
                                <small>Имеющих расширения: <b>${job.fileExtensions?join(", ")}</b></small>
                            </#if>
                        </td>
                        <td>
                            <#if job.running?? && job.running>
                                <b class="text-info">Выполняется</b>, обработано ${job.processed} файлов
                            <#elseif job.finished?has_content>
                                <b class="text-success">Завершён</b> ${job.finished?string("dd.MM.yyyy HH:mm")}
                                <#if job.processed??>
                                    <br />
                                    <small>обработано ${job.processed} файлов</small>
                                </#if>

                                <#if job.result??>
                                    <br />
                                    <small>найдено <a href="/jobs/${job.id!''}" title="Перейти к результатам поиска">${job.result?size}</a> дублей</small>
                                </#if>
                            <#else>
                                <b class="text-muted">Создан</b>
                            </#if>
                        </td>
                        <td>
                            ${job.created?string("dd.MM.yyyy HH:mm")}
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    <#else>
        <p class="text-info">
            Ничего не найдено
        </p>
    </#if>

</@util.page>