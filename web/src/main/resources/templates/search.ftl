<#setting date_format="dd.MM.yyyy">
<#import "util.ftl" as util />
<#import "/spring.ftl" as spring/>

<@util.page>

    <form action="/search" name="searchForm" method="post">

        <#if error??>
            <p class="text-danger">
                ${error}
            </p>
        </#if>

        <div class="form-group">
            <small>Директория поиска, например, C:/test</small>
            <input type="text" class="form-control" name= "scan_directory" id="scan_directory" aria-describedby="scan_directory_help" placeholder="Укажите директорию начала поиска" value="${scanDir!''}">
            <small id="scan_directory_help" class="form-text text-muted">Поиск дублей будет осуществлён внутри данной директории и всех её поддиректорий</small>
        </div>

        <div class="form-group">
            <small>Дублями считаются файлы, у которых совпадают:</small>

            <div class="form-check">
                <input class="form-check-input" type="radio" name="scan_type" id="scan_type_1" value="NAME_AND_SIZE" <#if !scanType?has_content || scanType=='NAME_AND_SIZE'>checked</#if> >
                <label class="form-check-label" for="scan_type_1">
                    Имя файла + размер
                </label>
            </div>

            <div class="form-check">
                <input class="form-check-input" type="radio" name="scan_type" id="scan_type_2" value="NAME_AND_SIZE_AND_HASH" <#if scanType?? && scanType=='NAME_AND_SIZE_AND_HASH'>checked</#if> >
                <label class="form-check-label" for="scan_type_2">
                    Имя файла + размер + хэш контента
                </label>
            </div>

            <div class="form-check">
                <input class="form-check-input" type="radio" name="scan_type" id="scan_type_3" value="SIZE_AND_HASH" <#if scanType?? && scanType=='SIZE_AND_HASH'>checked</#if> >
                <label class="form-check-label" for="scan_type_3">
                    Размер файла + хэш контента
                </label>
            </div>

        </div>

        <button type="submit" class="btn btn-primary">Запустить поиск</button>

    </form>

</@util.page>