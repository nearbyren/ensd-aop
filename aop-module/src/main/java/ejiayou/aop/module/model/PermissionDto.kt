package ejiayou.aop.module.model

/**
 * @author: lr
 * @created on: 2023/1/1 3:44 下午
 * @description:
 */
data class PermissionDto(val key: String, val description: Description)

data class Description(val title: String, val content: String)
