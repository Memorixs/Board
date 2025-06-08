function modifyBoard(button) {
    button.addEventListener("click", (e) => {
        const display = document.getElementById("displayTag");
        display.style.display = "none";
        const modify = document.getElementById("modifyTag");
        modify.style.display = "block";
    });
}

function confirmBoard(form, button, id) {
    button.addEventListener("click", async (e) => {
        e.preventDefault();
        try {
            const response = await modifyBoardApi(form, id);
            console.log("수정 완료: ", response);

            const display = document.getElementById("displayTag");
            display.style.display = "block";
            const modify = document.getElementById("modifyTag");
            modify.style.display = "none";

            // location.replace("http://localhost:8080/board/" + id);
            location.href = "/board/" + id;
        } catch(error) {
            alert(error.message);
        }
    })


}

const modifyBoardApi = async (form, id) => {
    const data = {
        title: form.title.value,
        content: form.content.value
    }

    const response = await fetch("http://localhost:8080/api/board/" + id, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)

    });
    if (response.status == 400) {

    }
    return response.json();
}

