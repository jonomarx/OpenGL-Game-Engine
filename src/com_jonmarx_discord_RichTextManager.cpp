#include <com_jonmarx_discord_RichTextManager.h>
#include <discord.h>
#include <iostream>
#include <stdlib.h>

struct thingy {
    char * state;
    char * details;
    char * type;
    char * name;
    char * instance;
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

void clean(struct thingy * strings) {
    //free(strings->details);
    //free(strings->name);
    //free(strings->state);
    //free(strings->type);
    //free(strings);
}

JNIEXPORT void JNICALL Java_com_jonmarx_discord_RichTextManager_pushRichText(JNIEnv * env, jobject obj, jlong ptr) {
    struct thingy * strings = (struct thingy*) (intptr_t) ptr;
    
    discord::Activity activity{};
    activity.SetState(strings->state);
    activity.SetDetails(strings->details);
    activity.SetType(discord::ActivityType::Playing);
    activity.SetInstance(strings->instance[0] == 't');
    activity.SetName(strings->name);
    
    discord::ActivityType type = discord::ActivityType::Playing;
    switch(strings->type[0]) {
        case 'p':
            type = discord::ActivityType::Playing;
            break;
        case 'l':
            type = discord::ActivityType::Listening;
            break;
        case 's':
            type = discord::ActivityType::Streaming;
            break;
        case 'w':
            type = discord::ActivityType::Watching;
            break;
    }
    
    
    core->ActivityManager().UpdateActivity(activity, [](discord::Result result) {
        
    });
    clean(strings);
    
    std::cout << "(JNI) Updated status.";
}
JNIEXPORT void JNICALL Java_com_jonmarx_discord_RichTextManager_tick(JNIEnv * env, jobject obj) {
    core->RunCallbacks();
}