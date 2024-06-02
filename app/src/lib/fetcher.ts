import { PUBLIC_SERVER_URL } from '$env/static/public';

let any: any

export default async function fetcher(
    url: string,
    {
        method = 'GET',
        body = any,
        token = '',
        apiUrlPrefix = true,
        stringify = true,
        noContentType = false,
        contentType = 'application/json;charset=UTF-8',
        headers = {}
    }

): Promise<Response> {
    let auth = '';
    let apiPrefix = '';
    let stringBody = '';

    if (apiUrlPrefix) apiPrefix = PUBLIC_SERVER_URL;

    if (token !== '') auth = 'Bearer ' + token;

    if (url[0] !== '/' && apiUrlPrefix) url = '/' + url;

    if (body && typeof body !== 'string') {
        stringBody = JSON.stringify(body);
    } else if (typeof body === 'string') {
        stringBody = body;
    }

    let requestContentType = {}

    if (!noContentType) {
        requestContentType = { 'Content-type': contentType }
    }

    const res = await fetch(apiPrefix + url, {
        method: method,
        body: method === "GET" ? null : stringify ? stringBody : body,
        headers: {
            Authorization: auth,
            ...requestContentType,
            ...headers
        },
        redirect: 'follow',
        cache: "no-cache"
    });

    if (res.redirected) {
        window.location.href = res.url;
    }

    return res
}
