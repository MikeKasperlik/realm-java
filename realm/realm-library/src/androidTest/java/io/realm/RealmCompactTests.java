/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm;

import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class RealmCompactTests {

    @Override
    protected void setUp() throws Exception {
        Realm.init(getContext());
    }


    public void testRealmCompact() {
        File directory = getContext().getExternalFilesDir(null);
        directory.mkdirs();

        assertTrue(directory.exists());

        RealmConfiguration config = new RealmConfiguration.Builder().directory(directory).name("somerealm.realm").build();
        Realm realm = Realm.getInstance(config);

        File realmFile = new File(config.getPath());
        assertTrue(realmFile.exists() && realmFile.canWrite());

        { // simple read/write
            Test test = new Test();

            realm.beginTransaction();
            try {
                realm.copyToRealm(test);

                realm.commitTransaction();
            } catch (Exception e) {
                realm.cancelTransaction();
                fail();
            }

            assertTrue(realm.where(Test.class).count() > 0);
        }

        realm.close();

        assertTrue(Realm.compactRealm(config));
    }
}
