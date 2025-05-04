package com.aricia.strelka.android

import com.aricia.strelka.android.Keys.KeysObject
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object DataBase {
    val supabase = createSupabaseClient(
        supabaseUrl = KeysObject.URL,
        supabaseKey = KeysObject.KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
    }
}