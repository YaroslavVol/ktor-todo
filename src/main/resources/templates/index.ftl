<#-- @ftlvariable name="todos" type="kotlin.collections.List<yarvol.models.Todo>" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <#list todos?reverse as todo>
        <div>
            <h3>
                <a href="/todos/${todo.id}">${todo.body}</a>
            </h3>
        </div>
    </#list>
    <hr>
    <p>
        <a href="/todos/new">Create todo</a>
    </p>
</@layout.header>