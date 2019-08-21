<#import "/spring.ftl" as spring/>

<#macro page>
    <!doctype html>
    <html lang="ru">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <link rel="stylesheet" href="<@spring.url '/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<@spring.url '/css/jquery.fancybox.min.css'/>">
        <script src="<@spring.url '/js/jquery-3.3.1.min.js'/>"></script>
        <script src="<@spring.url '/js/popper.min.js'/>"></script>
        <script src="<@spring.url '/js/bootstrap.min.js'/>"></script>
        <script src="<@spring.url '/js/jquery.fancybox.min.js'/>"></script>
        <script src="<@spring.url '/js/app.js'/>"></script>
    </head>

    <body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="<@spring.url '/'/>">
            Dupply
        </a>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<@spring.url '/'/>">Новый поиск</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<@spring.url '/jobs'/>">Созданные поиски</a>
                </li>
            </ul>
        </div>
    </nav>

    <div class="container-fluid">
        <#nested>
    </div>
    </body>
    </html>
</#macro>

<#function toHumanReadable num>
    <#if (num<=0) >
        <#return "не указан" />
    </#if>
    <#assign order = num?round?c?length />
    <#assign thousands = ((order - 1) / 3)?floor />
    <#if (thousands < 0)><#assign thousands = 0 /></#if>
    <#assign siMap = [ {"factor": 1, "unit": ""}, {"factor": 1000, "unit": "K"}, {"factor": 1000000, "unit": "M"}, {"factor": 1000000000, "unit":"G"}, {"factor": 1000000000000, "unit": "T"} ]/>
    <#assign siStr = (num / (siMap[thousands].factor))?string("0.#") + siMap[thousands].unit />
    <#return siStr />
</#function>

<#function scanTypeName type>
    <#if type?string == 'NAME_AND_SIZE'>
        <#return 'Имя файла и его размер' />
    <#elseif type?string == 'NAME_AND_SIZE_AND_HASH'>
        <#return 'Имя фалйа, его размер и хэш содержимого' />
    <#elseif type?string == 'SIZE_AND_HASH'>
        <#return 'Размер файла и хэш содержимого' />
    </#if>
    <#return '' />
</#function>

