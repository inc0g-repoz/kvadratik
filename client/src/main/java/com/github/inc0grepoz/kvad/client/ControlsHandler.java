package com.github.inc0grepoz.kvad.client;

import com.github.inc0grepoz.kvad.client.Controls.Key;
import com.github.inc0grepoz.kvad.entities.Camera;
import com.github.inc0grepoz.kvad.entities.Camera.CameraMode;
import com.github.inc0grepoz.kvad.entities.being.Anim;
import com.github.inc0grepoz.kvad.entities.being.Anim.Way;
import com.github.inc0grepoz.kvad.entities.being.Being;
import com.github.inc0grepoz.kvad.entities.level.Level;
import com.github.inc0grepoz.kvad.gui.Chat;

public class ControlsHandler {

    private final Controls controls;

    public ControlsHandler(Controls controls) {
        this.controls = controls;
    }

    public void onKeyTyped(char kc) {
        Chat chat = controls.getGame().getClient().getChat();
        if (chat.typing) {
            if (kc != '\u0008') {
                if (chat.locked) {
                    chat.locked = false;
                } else {
                    chat.draft += kc;
                }
            } else if (chat.draft.length() != 0) {
                chat.draft = chat.draft.substring(0, chat.draft.length() - 1);
            }
        }
    }

    public void onKeyPressed(Key key) {
        KvadratikGame game = controls.getGame();
        Level level = game.getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {
            Being player = level.getPlayer();

            if (player != null) {
                KvadratikClient client = game.getClient();

                // The chat code
                if (client.isConnected()) {
                    Chat chat = client.getChat();
                    if (chat.typing) {
                        if (key == Key.ENTER) {
                            chat.typing = false;
                            chat.send(chat.draft);
//                          chat.draft = null;
                        } else if (key == Key.ESCAPE) {
                            chat.typing = false;
//                          chat.draft = null;
                        }
                        return;
                    } else if (key == Key.CHAT) {
                        chat.draft = "";
                        chat.locked = true;
                        chat.typing = true;
                        return;
                    }
                }

                // Choosing a proper player animation
                player.move = Controls.isPlayerMoving();
                if (player.move) {
                    Anim anim = null;
                    boolean sprint = Key.SPRINT.pressed;
                    switch (key) {
                        case MOVE_UP:
                            anim = sprint ? Anim.RUN_W : Anim.WALK_W;
                            break;
                        case MOVE_DOWN:
                            anim = sprint ? Anim.RUN_S : Anim.WALK_S;
                            break;
                        case MOVE_LEFT:
                            anim = sprint ? Anim.RUN_A : Anim.WALK_A;
                            break;
                        case MOVE_RIGHT:
                            anim = sprint ? Anim.RUN_D : Anim.WALK_D;
                            break;
                        case SPRINT:
                            Anim ca = player.getAnim();
                            if (ca == Anim.WALK_W) {
                                anim = Anim.RUN_W;
                            } else if (ca == Anim.WALK_S) {
                                anim = Anim.RUN_S;
                            } else if (ca == Anim.WALK_A) {
                                anim = Anim.RUN_A;
                            } else if (ca == Anim.WALK_D) {
                                anim = Anim.RUN_D;
                            }
                            break;
                        default:
                    }
                    player.applyAnim(anim);
                }

                // Defining the camera moving direction
                Camera camera = level.getCamera();
                camera.move = camera.mode == CameraMode.FREE
                        && Controls.isCameraMoving();
                if (camera.move) {
                    switch (key) {
                        case SELECT_UP:
                            camera.moveDirection = Way.W;
                            break;
                        case SELECT_DOWN:
                            camera.moveDirection = Way.S;
                            break;
                        case SELECT_LEFT:
                            camera.moveDirection = Way.A;
                            break;
                        case SELECT_RIGHT:
                            camera.moveDirection = Way.D;
                            break;
                        default:
                    }
                }
            }
        }
    }

    public void onKeyReleased(Key key) {
        Level level = controls.getGame().getLevel();

        if (level == null) {
            // TODO: Some menu code here
        } else {
            Being player = level.getPlayer();

            if (player != null) {
                player.move = Controls.isPlayerMoving();
                if (player.move) {
                    Anim anim = null;
                    boolean sprint = Key.SPRINT.pressed;
                    if (Key.MOVE_UP.pressed) {
                        anim = sprint ? Anim.RUN_W : Anim.WALK_W;
                    } else if (Key.MOVE_DOWN.pressed) {
                        anim = sprint ? Anim.RUN_S : Anim.WALK_S;
                    } else if (Key.MOVE_LEFT.pressed) {
                        anim = sprint ? Anim.RUN_A : Anim.WALK_A;
                    } else if (Key.MOVE_RIGHT.pressed) {
                        anim = sprint ? Anim.RUN_D : Anim.WALK_D;
                    }
                    player.applyAnim(anim);
                } else {
                    player.playIdleAnim();
                }

                // Stopping the camera if it's needed
                Camera camera = level.getCamera();
                camera.move = Controls.isCameraMoving();
                if (!camera.move) {
                    camera.moveDirection = null;
                }
            }
        }
    }

}
