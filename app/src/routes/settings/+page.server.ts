import fetcher from '$lib/fetcher'
import { redirect } from '@sveltejs/kit'

export async function load({ cookies }) {
    const token = cookies.get('jwtToken')

    const res = await fetcher(`/account`, {
        token: token
    })

    if (!res.ok) {
        redirect(308, "/404")
    }
    
    const body = await res.json()
    return {
        account: body
    }
}
