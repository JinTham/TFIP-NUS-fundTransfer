package tfip.paf.fundTransfer2.Repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;


@Repository
public class LogAuditRepository {
    
    @Autowired @Qualifier("my-redis")
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveToRedis(String txId, JsonObject json) {
        redisTemplate.opsForValue().set(txId,json.toString());
    }

    public void saveToMongo(String txId, JsonObject json) {
        Document doc = new Document();
        doc.put(txId,json);
        mongoTemplate.insert(doc,"transaction");
    }

}
