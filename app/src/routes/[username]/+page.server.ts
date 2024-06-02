import fetcher from '$lib/fetcher.js'
import { redirect } from '@sveltejs/kit'

export async function load({ params }) {
    const res = await fetcher(`profile/${params.username}`, {})

    if (!res.ok) {
        redirect(308, "/404")
    }
    
    const body = await res.json()
    return {
        profile: body
    }
}
