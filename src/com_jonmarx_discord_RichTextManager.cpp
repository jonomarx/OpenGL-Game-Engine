#include <com_jonmarx_discord_RichTextManager.h>
#include <discord.h>
#include <iostream>

struct thingy {
    char * state;
    char * details;
    char * type;
    char * name;
};

discord::Core* core{};
JNIEXPORT void JNICALL Java_com_jonmarx_discord_RichTextManager_init(JNIEnv * env, jobject obj) {
    auto result = discord::Core::Create(885668222643367956, DiscordCreateFlags_Default, &core);
    std::cout << "(JNI) discord has loaded!\n";
    std::cerr << "(JNI) does this print?\n";
    switch(result) {
        case discord::Result::Ok:
            std::cout << "Status: Ok!\n";
            break;
        default:
            std::cout << "Status: Not Ok!\n";
    };
}
JNIEXPORT void JNICALL Java_com_jonmarx_discord_RichTextManager_pushRichText(JNIEnv * env, jobject obj, jlong ptr) {
    struct thingy * strings = (struct thingy*) (intptr_t) ptr;
    
    discord::Activity activity{};
    activity.SetState(strings->state);
    activity.SetDetails(strings->details);
    activity.SetType(discord::ActivityType::Playing);
    //activity.SetInstance(false);
    activity.SetName(strings->name);
    
    core->ActivityManager().UpdateActivity(activity, [](discord::Result result) {
        
    });
    
    std::cout << "(JNI) Updated status.";
}
JNIEXPORT void JNICALL Java_com_jonmarx_discord_RichTextManager_tick(JNIEnv * env, jobject obj) {
    core->RunCallbacks();
}