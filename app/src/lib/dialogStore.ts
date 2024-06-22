import { writable } from "svelte/store";

export type dialog = "registration" | "login" | "changeEmail" | "changePassword" | null

export const dialogStroe = writable<dialog>()

export const updateDialog = (state: dialog) => {
    dialogStroe.update((s) => s = state)
}