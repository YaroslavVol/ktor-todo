<#-- @ftlvariable name="todos" type="kotlin.collections.List<yarvol.models.Todo>" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
            <h3>
                ${todo.title}
            </h3>
            <p>
                ${todo.body}
            </p>
            <hr>
            <p>
                <a href="/todos/${todo.id}/edit">Edit todo</a>
            </p>
        </div>
</@layout.header>