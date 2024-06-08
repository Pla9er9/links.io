import fetcher from "$lib/fetcher";
import { error } from "@sveltejs/kit";

export async function POST({request, params, cookies}) {
        const data = await request.json();

        const slug = params.slug ?? ""
        checkIfRouteIsCorrect(slug)

        const response = await fetcher(`/auth/${slug}`, {
            method: 'POST',
            body: data,
        })

        if (!response) {
            throw error(404)
        }
        if (response?.status !== 200) {
            const message = await response.json()
            throw error(response.status, message)
        }

        const body = await response.json()

        cookies.set('jwtToken', body.token, {
            path: '/',
            httpOnly: true,
            sameSite: 'strict',
            secure: true,
            maxAge: 60 * 60 * 24 * 365 * 10
        });
        return new Response();
};

function checkIfRouteIsCorrect(slug: string) {
    if (slug !== "login" && slug !== "register") {
        throw error(404)
    }
}