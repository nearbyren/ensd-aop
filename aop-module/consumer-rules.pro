-keepclasseswithmembers class * {
    @ejiayou.aop.module.permis.PermissionBefore <methods>;
}

-keepclasseswithmembers class * {
    @ejiayou.aop.module.permis.PermissionDenied <methods>;
}

-keepclasseswithmembers class * {
    @ejiayou.aop.module.permis.PermissionNoAskDenied <methods>;
}

-keepclasseswithmembers class * {
    @ejiayou.aop.module.permis.GoPermission <methods>;
}

-keepclasseswithmembers class * {
    @ejiayou.aop.module.permis.Permission <methods>;
}