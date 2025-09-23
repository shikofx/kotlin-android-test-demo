# Проблемы, возникшие при первоначальной настройке и запуске проекта

В этом документе зафиксированы основные проблемы, которые были решены для успешной сборки и запуска проекта.

## 1. Устаревший API для ViewModel

### Проблема
Первоначальная сборка проекта завершалась с ошибкой компиляции:
```
error: cannot find symbol
import androidx.lifecycle.ViewModelProviders;
```
Это происходило в файлах `SplashActivity.java`, `ProductCatalogFragment.java` и `ProductDetailFragment.java`. Причина заключалась в использовании устаревшего класса `ViewModelProviders`, который был удален в новых версиях библиотеки `androidx.lifecycle`.

### Решение
Класс `ViewModelProviders` был заменен на `ViewModelProvider`.

**Пример изменения:**

*Было:*
```java
viewModel = ViewModelProviders.of(this, new SplashViewModelFactory(this.getApplication())).get(SplashViewModel.class);
```

*Стало:*
```java
viewModel = new ViewModelProvider(this, new SplashViewModelFactory(this.getApplication())).get(SplashViewModel.class);
```
Также был обновлен импорт с `androidx.lifecycle.ViewModelProviders` на `androidx.lifecycle.ViewModelProvider`.

## 2. Недоступность удаленных зависимостей

### Проблема
После исправления первой проблемы была предпринята попытка заменить локальные `.aar` библиотеки на удаленные зависимости из репозитория JitPack. Это привело к новым ошибкам сборки:
```
Could not find com.github.kyanogen:signature-view:1.2.
Could not resolve com.github.williamww:silky-signature-pad:1.1.0. (401 Unauthorized)
```
Ошибки указывали на то, что JitPack не может предоставить данные артефакты.

### Решение
Было принято решение вернуться к исходной конфигурации с использованием локальных `.aar` файлов, хранящихся в папке `app/libs`.

В файле `app/build.gradle` были удалены неработающие удаленные зависимости и восстановлены строки для подключения локальных библиотек:
```gradle
// ...
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    // ...
    implementation(name:'com.kyanogen.signatureview.signature-view', ext:'aar')
    implementation(name:'com.williamww.silky-signature', ext:'aar')
    implementation(name:'com.uphyca.creditcardedittext', ext:'aar')
    // ...
}
```
Несмотря на предупреждение `Using flatDir should be avoided`, этот подход является единственным рабочим для данных библиотек, так как они отсутствуют в централизованных репозиториях.

