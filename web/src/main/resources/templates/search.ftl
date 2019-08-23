<#setting date_format="dd.MM.yyyy">
<#import "util.ftl" as util />
<#import "/spring.ftl" as spring/>

<@util.page>

    <div class="card" style="margin-left: 5px; margin-top: 10px; width: 800px">
        <form action="/search" name="searchForm" method="post">

            <#if error??>
                <div class="card card-body">
                    <p class="text-danger">
                        ${error}
                    </p>
                </div>
            </#if>

            <div class="card card-body bg-light">
                <div class="form-group">
                    <small>Директория поиска, например, C:/test</small>
                    <input type="text" class="form-control" name= "scan_directory" id="scan_directory" aria-describedby="scan_directory_help" placeholder="Укажите директорию начала поиска" value="${scanDir!''}">
                    <small id="scan_directory_help" class="form-text text-muted">Поиск дублей будет осуществлён внутри данной директории и всех её поддиректорий</small>
                </div>
            </div>

            <br />

            <div class="card card-body bg-light">
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
            </div>

            <br />

            <div class="card card-body bg-light">
                <div class="form-group">
                    <small>Только дубли со следующими расширениями:</small>
                    <table cellspacing="0" cellpadding="0">
                        <tr>
                            <td>
                                <div style="margin-right: 10px">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_jpg"
                                               value=".jpg" <#if fileExtensions?? && fileExtensions?seq_contains('.jpg')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_jpg">.jpg</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_jpeg"
                                               value=".jpeg" <#if fileExtensions?? && fileExtensions?seq_contains('.jpeg')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_jpeg">.jpeg</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_png"
                                               value=".png" <#if fileExtensions?? && fileExtensions?seq_contains('.png')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_png">.png</label>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div style="margin-right: 10px">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_mp3"
                                               value=".mp3" <#if fileExtensions?? && fileExtensions?seq_contains('.mp3')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_mp3">.mp3</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_avi"
                                               value=".avi" <#if fileExtensions?? && fileExtensions?seq_contains('.avi')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_avi">.avi</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_mov"
                                               value=".mov" <#if fileExtensions?? && fileExtensions?seq_contains('.mov')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_avi">.mov</label>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div style="margin-right: 10px">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_zip"
                                               value=".zip" <#if fileExtensions?? && fileExtensions?seq_contains('.zip')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_zip">.zip</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_rar"
                                               value=".rar" <#if fileExtensions?? && fileExtensions?seq_contains('.rar')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_rar">.rar</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="file_extensions" id="file_extension_7z"
                                               value=".7z" <#if fileExtensions?? && fileExtensions?seq_contains('.7z')>checked</#if> >
                                        <label class="form-check-label" for="file_extension_7z">.7z</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <br />

            <button type="submit" class="btn btn-primary">Запустить поиск</button>

        </form>
    </div>

</@util.page>