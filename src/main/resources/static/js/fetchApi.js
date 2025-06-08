async function signin() {
        const data = {
            email: form.email.value,
            password: form.password.value
        };

        const response = await fetch("http://localhost:8080/api/signin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
        })
    if(response.status == 400) {
        console.log(response);
        throw new Error("존재하지 않는 회원 입니다.")
    } else if(response.status == 500) {
        console.log(response);
        throw new Error("서버내부 에러")
    }
    return response.text();


}


//fetch 메서드 요청이 Ajax 기술 중 하나임
//JS가 싱글스레드 언어인데 비동기 작업이 어떻게 가능? web api가 멀티 스레드임. 서버에게 리소스 요청하거나 파일 입출력 등을 Web API가 받아서 처리해줌. 브라우저라는 소프트웨어가 멀티스레드이므로 메인 자바 스크립트 스레드를 차단하지 않고 다른 스레드를 사용하여 동시 처리 가능
async function signup(form) {
        const data = {
            email: form.email.value,
            password: form.password.value
        }
    const response = await fetch("http://localhost:8080/api/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data),
    })//. 프로미스를 사용하면 비동기 메서드에서 마치 동기 메서드처럼 값을 반환할 수 있습니다. 다만 최종 결과를 반환하는 것이 아니고, 미래의 어떤 시점에 결과를 제공하겠다는 '프로미스(promise)'를 반환합니다.
    if(response.status == 400) {
        console.log(response);
        throw new Error("이미 존재하는 홰원입니다.");
    } else if(respones.status == 500) {
        console.log(response);
        throw new Error("서버 내부 오류")
    }
    return response.text();

}

async function deleteBoard(id) {
    const response = await fetch("http://localhost:8080/api/board/" + id, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
    });
    if(response.status == 400) {
        console.log(response)
        throw new Error("삭제할 권한이 없습니다.");
    } else if(response.status == 500) {
        console.log(response);
        throw new Error("서버 내부 에러")
    }
     return response.text();

}

//비동기 함수 -> 언제끝날지 모르는 작업, 이전 작업의 완료 여부를 확인하지 않고 실행
//왜 이 요청을 비동기로 실행하는가? 서버는 멀티스레드, 여러 요청을 받아서 처리할 수 있음. 이걸 동기로 작업하면? 서버의 멀티스레드 사용에 의미가 없다.
//그럼 요청은 다 비동기로 처리하는 게 맞나?
async function createBoard(form) {

        const data = {
            title: form.title.value,
            content: form.title.value
        }

        console.log("게시글 생성 함수 실행")
        //response는 Response 객체로 이행하는 Promise이다.
        const response = await fetch("http://localhost:8080/api/board", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        // const id = await response.json();
        return response;
}

async function logout() {

    const response = await fetch("http://localhost:8080/api/logout", {
        method: "POST",
    })
    const data = await response.text();
    if(response.status == 400) {
        console.log(response);
        throw new Error(data);
    }
    return data;

}

async function deleteAccount() {
    const response  = await fetch("http://localhost:8080/api/users", {
        method: "DELETE"
    })
    const data = await response.text();
    if(response.status == 400) {
        throw new Error(data);
    }

    return data;
}




